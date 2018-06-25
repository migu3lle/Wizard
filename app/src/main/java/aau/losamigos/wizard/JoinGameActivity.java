package aau.losamigos.wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.Random;

import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.network.ICallbackAction;
import aau.losamigos.wizard.network.NetworkHelper;
import aau.losamigos.wizard.types.Fractions;

public class JoinGameActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

    ListView lvHosts;
    SalutListViewAdapter adapter;
    ArrayList<SalutDevice> hostList;
    Button btnDiscover;
    EditText etClientName;

    private SalutDataReceiver dataReceiver;
    private SalutServiceData serviceData;
    private Salut network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        btnDiscover = findViewById(R.id.btn_Discover);
        btnDiscover.setOnClickListener(this);
        lvHosts = findViewById(R.id.lv_Hosts);
        lvHosts.setOnItemClickListener(this);
        etClientName = findViewById(R.id.et_ClientName);
        etClientName.setText(Fractions.getRandomFraction());
        //Remove focus from EditText
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        hostList = new ArrayList<>();
        adapter = new SalutListViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, hostList);
        lvHosts.setAdapter(adapter);

        //Handles received messages from host device
        DataCallback dataCallback = new DataCallback();

        dataCallback.addFireOnceCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                Toast.makeText(getApplicationContext(), "Message received", Toast.LENGTH_SHORT).show();

                nextActivity(message.cheatEnabled);
            }
        });

        /*Create a data receiver object that will bind the callback
        with some instantiated object from our app. */
        dataReceiver = new SalutDataReceiver(this, dataCallback);

        /*Populate the details for our awesome service. */
        serviceData = new SalutServiceData("wizardService", NetworkHelper.findFreePort(), etClientName.getText().toString());

        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                // wiFiFailureDiag.show();
                // OR
                Log.e("WizardApp", "Sorry, but this device does not support WiFi Direct.");
            }
        });


        GameConfig.getInstance().setSalut(network, dataCallback);
    }

    /*
    Handle Button events on this Activity
    */
    @Override
    public void onClick(View v) {
        Log.d("WizardApp", "button clicked...");
        if (v.getId() == R.id.btn_Discover) {
            discoverServices();
        }
    }

    private void discoverServices() {
        Log.d("WizardApp", "want start discovering now...");
        if (!network.isRunningAsHost && !network.isDiscovering) {
            Log.d("WizardApp", "start discovering...");
            network.discoverNetworkServices(new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(getApplicationContext(), "Device: " + network.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();
                    hostList.clear();
                    for (SalutDevice device : network.foundDevices) {
                        hostList.add(device);
                    }
                    adapter.notifyDataSetChanged();
                }
            }, true);
            btnDiscover.setText("Stop Discovery");
            etClientName.setEnabled(false);
        } else {
            Log.d("WizardApp", "else...");
            network.stopServiceDiscovery(true);
            btnDiscover.setText("DISCOVER");
            etClientName.setEnabled(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final SalutDevice device = (SalutDevice) parent.getItemAtPosition(position);
        view.animate().setDuration(2000).alpha(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        etClientName.setEnabled(false);
                        btnDiscover.setClickable(false);
                        registerWithHost(device);
                        Log.d("WizardApp", "Now i want to connect to device " + device.toString());
                    }
                });
    }

    public void registerWithHost(SalutDevice host){
        network.registerWithHost(host, new SalutCallback() {
            @Override
            public void call() {
                Log.d("WizardApp", "We're now registered.");
                //Once registered with a host, no other host service should be selectable
                lvHosts.setClickable(false);
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d("WizardApp", "We failed to register.");
                etClientName.setEnabled(true);
                btnDiscover.setClickable(true);
            }
        });
    }

    private void nextActivity(boolean b) {
        Intent intent = new Intent(this, TableActivity.class);
        intent.putExtra("CheatEnabled", b);
        startActivity(intent);

    }
}