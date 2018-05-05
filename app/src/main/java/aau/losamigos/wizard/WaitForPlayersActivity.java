package aau.losamigos.wizard;

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

    Button btnService;
    Button btnBack;
    Button btnStartGame;

    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    Salut network;

    private boolean gameStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

        //For client list view....
        lvClients = findViewById(R.id.lv_Players);
        clientList = new ArrayList<>();
        adapter = new SalutListViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, clientList);
        lvClients.setAdapter(adapter);

        //Some buttons...
        btnService = findViewById(R.id.btn_Service);
        btnService.setOnClickListener(this);
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
        System.out.println("Hello: " + GameConfig.getInstance().getName());
        Log.d("WizardApp", "Game Name: " + GameConfig.getInstance().getName());
        serviceData = new SalutServiceData("testAwesomeService", NetworkHelper.findFreePort(), GameConfig.getInstance().getName());

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

        GameConfig.getInstance().setSalut(network, dataCallback);
        //Add this device (=host) to clientList
        clientList.add(network.thisDevice);
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
            btnService.setText("Stop Service");

        }
        else
        {
            network.stopNetworkService(false);
            btnService.setText("Start Service");
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(this, TestMessageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(network != null) {
            if( network.isRunningAsHost) {
                try {
                    network.stopNetworkService(true);

                } catch (Exception e) {

                }
            } else {
                try {
                    network.unregisterClient(true);
                } catch (Exception e) {

                }
            }
        }
    }
}
