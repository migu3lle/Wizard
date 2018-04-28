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
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

public class LaunchGameActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener{

    ListView lvHosts;
    ArrayAdapter adapter;
    ArrayList<String> hostList;
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

        hostList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, hostList);
        lvHosts.setAdapter(adapter);

        /*Create a data receiver object that will bind the callback
        with some instantiated object from our app. */
        dataReceiver = new SalutDataReceiver(this, this);

        /*Populate the details for our awesome service. */
        serviceData = new SalutServiceData("testAwesomeService", 60606, "HOST");

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
        if(v.getId() == R.id.btn_Discover){
            discoverServices();
        }
    }

    private void discoverServices()
    {
        Log.d("Tag", "want start discovering now...");
        if(!network.isRunningAsHost && !network.isDiscovering)
        {
            Log.d("Tag", "start discovering...");
            network.discoverNetworkServices(new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(getApplicationContext(), "Device: " + network.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();

                    for (SalutDevice device : network.foundDevices) {
                        hostList.add(device.instanceName);
                    }
                    adapter.notifyDataSetChanged();
                }
            }, true);
            btnDiscover.setText("Stop Discovery");
        }
        else
        {
            Log.d("Tag", "else...");
            network.stopServiceDiscovery(true);
            btnDiscover.setText("DISCOVER");
        }
    }

    @Override
    public void onDataReceived(Object o) {

    }
}

