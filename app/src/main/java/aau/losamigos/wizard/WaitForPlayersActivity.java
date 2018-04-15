package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import aau.losamigos.wizard.elements.Player;

public class WaitForPlayersActivity extends AppCompatActivity {

    private ListView listViewPlayers = findViewById(R.id.lv_players);
    private ArrayAdapter<Player> arrayAdapter;
    private Player[] players;
    private ArrayList<Player> playerList = new ArrayList<Player>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configStartGameButton();
        configBackButton();

        createWifiConnection();

        addPlayersToList();
        generateAdapter();

    }

    private void configBackButton() {
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();   //TODO might be better to use onPause(), needs to be checked;
            }
        });
    }

    private void configStartGameButton() {
        Button btnStartGame = (Button) findViewById(R.id.btn_startGame);
        btnStartGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                TODO start new Game, to be clarified with Andreas
                 */
            }
        });
    }


    /**
     * ....to be clarified with Stefan
     */
    private void createWifiConnection(){
        /*
        TODO connect to adapter view in activity_wait_for_players and fill with data from WIFI connection
         */
    }

    /**
     * Adds new player to local arrayList of Players shown in ListView
     */
    private void addPlayersToList(){
        for (int i = 0; i < players.length; i++) {
            playerList.add(players[i]);
        }
    }

    /**
     * Adapter manages the data model (Player) and adapts it to the individual entries in ListView
     */
    private void generateAdapter(){
        arrayAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, players);
        listViewPlayers.setAdapter(arrayAdapter);
    }
}
