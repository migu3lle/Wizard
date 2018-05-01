package aau.losamigos.wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import aau.losamigos.wizard.base.GameConfig;

public class JoinGameActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener, ListView.OnItemClickListener {

    ListView lvHosts;
    ArrayAdapter adapter;
    ArrayList<SalutDevice> hostList;
    Button btnDiscover;

    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public Salut network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_game);


        btnDiscover = findViewById(R.id.btn_Discover);
        btnDiscover.setOnClickListener(this);
        lvHosts = findViewById(R.id.lv_Hosts);
        lvHosts.setOnItemClickListener(this);

        hostList = new ArrayList<>();
        adapter = new ArrayAdapter<SalutDevice>(getApplicationContext(), android.R.layout.simple_list_item_1, hostList);
        lvHosts.setAdapter(adapter);

        /*Create a data receiver object that will bind the callback
        with some instantiated object from our app. */
        dataReceiver = new SalutDataReceiver(this, this);

        /*Populate the details for our awesome service. */
        serviceData = new SalutServiceData("testAwesomeService", 60606, "CLIENT");

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
        Log.d("Tag", "button clicked...");
        if (v.getId() == R.id.btn_Discover) {
            discoverServices();
        }
    }

    private void discoverServices() {
        Log.d("Tag", "want start discovering now...");
        if (!network.isRunningAsHost && !network.isDiscovering) {
            Log.d("Tag", "start discovering...");
            network.discoverNetworkServices(new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(getApplicationContext(), "Device: " + network.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();

                    for (SalutDevice device : network.foundDevices) {
                        hostList.add(device);
                    }
                    adapter.notifyDataSetChanged();
                }
            }, true);
            btnDiscover.setText("Stop Discovery");
        } else {
            Log.d("Tag", "else...");
            network.stopServiceDiscovery(true);
            btnDiscover.setText("DISCOVER");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final SalutDevice device = (SalutDevice) parent.getItemAtPosition(position);
        view.animate().setDuration(2000).alpha(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        registerWithHost(device);
                        System.out.println("Now i want to connect to device " + device.toString());
                    }
                });
    }

    public void registerWithHost(SalutDevice host){
        network.registerWithHost(host, new SalutCallback() {
            @Override
            public void call() {
                System.out.println("We're now registered.");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                System.out.println("We failed to register.");
            }
        });
    }

    @Override
    public void onDataReceived(Object o) {
        Toast.makeText(getApplicationContext(), "Message received", Toast.LENGTH_SHORT).show();
        GameConfig.getInstance().setSalut(network);
        nextActivity();
    }

    private void nextActivity() {
        Intent intent = new Intent(this, TestMessageActivity.class);
        startActivity(intent);
    }
}