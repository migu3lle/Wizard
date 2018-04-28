package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import aau.losamigos.wizard.elements.Player;

public class WaitForPlayersActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener{


    ListView listViewPlayers;
    Button btnService;
    Button btnBack;
    Button btnStartGame;

    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    Salut network;

    ArrayAdapter<Player> arrayAdapter;
    ArrayList<Player> playerList;

    private boolean gameStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

        //Initialize buttons
        listViewPlayers = findViewById(R.id.lv_Players);
        btnService = findViewById(R.id.btn_Service);
        btnService.setOnClickListener(this);
        btnBack = findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(this);
        btnStartGame = findViewById(R.id.btn_StartGame);
        btnStartGame.setOnClickListener(this);

        //For List view....
        playerList = new ArrayList<>();
        createAdapter();

        /*Create a data receiver object that will bind the callback
        with some instantiated object from our app. */
        dataReceiver = new SalutDataReceiver(this, this);

        /*Populate the details for our awesome service. */
        serviceData = new SalutServiceData("testAwesomeService", 60606, "HOST");

        /*Create an instance of the Salut class, with all of the necessary data from before.
        * We'll also provide a callback just in case a device doesn't support WiFi Direct, which
        * Salut will tell us about before we start trying to use methods.*/
        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                // wiFiFailureDiag.show();
                // OR
                Log.e("WizardApp", "Sorry, but this device does not support WiFi Direct.");
            }
        });
    }

    /*
        Handle Button events on this Activity
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Service){
            //Start a host service
            setupNetwork();
        }
        else if(v.getId() == R.id.btn_Back){
            finish();
        }
        else if(v.getId() == R.id.btn_StartGame){
            gameStarted = true;
            //TODO: GameConfig.getInstance().setPlayers(playerList);
                /*
                TODO start new Game, to be clarified with Andi
                 */
        }

    }

    private void setupNetwork()
    {
        Log.d("WizardApp", "Starte Netzwerk Host Service....");
        if(!network.isRunningAsHost)
        {
            Log.d("WizardApp", "Network services started");
            network.startNetworkService(new SalutDeviceCallback() {
                @Override
                public void call(SalutDevice salutDevice) {
                    Toast.makeText(getApplicationContext(), "Device: " + salutDevice.instanceName + " connected.", Toast.LENGTH_SHORT).show();
                }
            });
            btnService.setText("Stop Service");

        }
        else
        {
            network.stopNetworkService(false);
            btnService.setText("Start Service");
        }
    }


    /**
     * Adds new player to ListView e.g. if player joined
     * Returns true if player was added to Queue; else: false
     */

    public boolean addPlayerToQueue(Player newPlayer) {
        if (gameStarted)
            return false;
        else {
            playerList.add(newPlayer);
            return true;
        }
    }


    /**
     * Removes player from ListView e.g. if Player disconnected
     * Returns true if player was removed from Queue; else: false
     */
    public boolean removePlayerFromQueue(Player playerToRemove) {
        if (gameStarted)
            return false;
        else {
            playerList.remove(playerToRemove);
            return true;
        }
    }

    /**
     * Adapter manages the data model (Player) and adapts it to the individual entries in ListView
     */
    private void createAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerList);
        listViewPlayers.setAdapter(arrayAdapter);
    }


    @Override
    public void onDataReceived(Object o) {

    }
}
