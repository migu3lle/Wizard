package aau.losamigos.wizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.elements.Player;

public class WaitForPlayersActivity extends AppCompatActivity {



    private static ListView listViewPlayers;
    private ArrayAdapter<Player> arrayAdapter;
    private ArrayList<Player> playerList;

    private boolean gameStarted;

    static List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    static String [] deviceNameArray;
    static WifiP2pDevice [] deviceArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

        listViewPlayers = findViewById(R.id.lv_players);
        playerList = new ArrayList<>();


        configStartGameButton();
        configBackButton();
        createAdapter();

    }

    private void configBackButton() {
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configStartGameButton() {
        Button btnStartGame = (Button) findViewById(R.id.btn_startGame);
        btnStartGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameStarted = true;
                GameConfig.getInstance().setPlayers(playerList);
                /*
                TODO start new Game, to be clarified with Andi
                 */
            }
        });
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
    /*
     initialize a WiFi Discover to wait for Ready Player One
    */
    private void createWifiConnection(){

        MainActivity.mManager.discoverPeers(MainActivity.mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });


        /*
        TODO connect to adapter view in activity_wait_for_players and fill with data from WIFI connection
         */
    }

      WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {


        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers)){

                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];

                int index =0;

                for(WifiP2pDevice device: peerList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index]= device;
                    index++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                listViewPlayers.setAdapter(adapter);
            }

            if(peers.size() == 0){
                Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_SHORT).show();
                return;
            }

        }
    };

    /**
     * Removes player from ListView e.g. if Player disconnected
     * Returns true if player was removed from Queue; else: false
     */
    public boolean removePlayerFromQueue(Player playerToRemove){
        if(gameStarted)
            return false;
        else{
            playerList.remove(playerToRemove);
            return true;
        }
    }

    /**
     * Adapter manages the data model (Player) and adapts it to the individual entries in ListView
     */
    private void createAdapter(){
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerList);
        listViewPlayers.setAdapter(arrayAdapter);
    }


}
