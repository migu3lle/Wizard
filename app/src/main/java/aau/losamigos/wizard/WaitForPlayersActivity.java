package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import aau.losamigos.wizard.elements.Player;

public class WaitForPlayersActivity extends AppCompatActivity {

    private ListView listViewPlayers;
    private ArrayAdapter<Player> arrayAdapter;
    private ArrayList<Player> playerList;
    private boolean gameStarted;

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
    public boolean addPlayerToQueue(Player newPlayer){
        if(gameStarted)
            return false;
        else{
            playerList.add(newPlayer);
            return true;
        }
    }

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
