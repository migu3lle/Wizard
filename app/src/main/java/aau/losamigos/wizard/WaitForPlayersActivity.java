package aau.losamigos.wizard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.network.NetworkHelper;

public class WaitForPlayersActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lvClients;
    SalutListViewAdapter adapter;
    ArrayList<SalutDevice> clientList;

    Button btnBack;
    Button btnStartGame;

    Bundle bundle;

    private SalutDataReceiver dataReceiver;
    private SalutServiceData serviceData;
    private Salut network;

    private boolean gameStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

        bundle = getIntent().getExtras();

        //For client list view....
        lvClients = findViewById(R.id.lv_Players);
        clientList = new ArrayList<>();
        adapter = new SalutListViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, clientList);
        lvClients.setAdapter(adapter);

        //Some buttons...
        btnBack = findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(this);
        btnStartGame = findViewById(R.id.btn_StartGame);
        btnStartGame.setOnClickListener(this);

        //Handles received messages from other devices
        DataCallback dataCallback = new DataCallback();

        /*Create a data receiver object that will bind the callback
        with some instantiated object from our app. */
        dataReceiver = new SalutDataReceiver(this, dataCallback);

        /*Populate the details for our service. */
        serviceData = new SalutServiceData("wizardService", NetworkHelper.findFreePort(), bundle.getString("hostPlayerName"));

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

        //Store Salut object in GameConfig
        GameConfig.getInstance().setSalut(network, dataCallback);
        //Add this device (=host) to clientList
        clientList.add(network.thisDevice);
        setupNetwork();
    }

    /*
    Handle Button events on this Activity
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Back){
            this.onBackPressed();
        }
        else if(v.getId() == R.id.btn_StartGame){
            if(!checkMinPlayer())
                return;
            gameStarted = true;
            GameConfig.getInstance().setPlayers(clientList);
            GameConfig.getInstance().setIsHost(true);


            Message myMessage = new Message();
            myMessage.description = "Los gehts";

            network.sendToAllDevices(myMessage, new SalutCallback() {
                @Override
                public void call() {
                    Log.e("WizardApp", "Oh no! The data failed to send.");
                }
            });
            nextActivity();
        }
    }

    /** Checks if List View contains enough players (GameConfig.MinPlayer)
     * @return true if enough players, false if not
     */
    private boolean checkMinPlayer(){
        if(clientList.size() < GameConfig.getInstance().getMinPlayer()){
            Toast.makeText(getApplicationContext(), R.string.toast_too_little_players, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupNetwork()
    {
        Log.d("WizardApp", "Starte Netzwerk Host Service....");
        if(!network.isRunningAsHost)
        {
            System.out.println("Network services started");
            network.startNetworkService(new SalutDeviceCallback() {
                @Override
                public void call(SalutDevice salutDevice) {
                    Toast.makeText(getApplicationContext(), "Device: " + salutDevice.readableName + " connected.", Toast.LENGTH_SHORT).show();
                    clientList.add(salutDevice);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("WizardApp", "WaitForPlayersActivity: onDestroy()");

        if(network != null) {
            if( network.isRunningAsHost) {
                try{
                    /* For any reason cancelConnecting() is needed first to make stopNetworkService functional
                     * This triggers the WifiP2pManager.stopServiceRequest() method internal.
                     * Otherwise stopNetworkService throws an SocketException when calling ServerSocket.close() method */
                    Log.d("WizardApp", "WaitForPlayersActivity: Trying cancelConnecting()");
                    network.cancelConnecting();
                } catch (Exception e){
                    Log.e("Exception:", e.getMessage());
                }
                try {
                    Log.d("WizardApp", "WaitForPlayersActivity: Trying stopNetworkService()");
                    network.stopNetworkService(true);

                } catch (Exception e) {
                    Log.e("Exception:", e.getMessage());
                }
            } else {
                try {
                    Log.d("WizardApp", "WaitForPlayersActivity: Trying unregisterClient()");
                    network.unregisterClient(true);
                } catch (Exception e) {
                    Log.e("Exception:", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.AlertBack)
                .setMessage(R.string.AlertBackDescription)
                .setPositiveButton(R.string.AlertBackYes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameConfig.getInstance().reset();
                        finish();
                    }
                })
                .setNegativeButton(R.string.AlertBackNo, null)
                .show();
    }
}
