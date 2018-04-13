package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import aau.losamigos.wizard.elements.Player;

public class WaitForPlayersActivity extends AppCompatActivity {

    private ArrayAdapter<Player> arrayAdapter;
    private Player[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);


        arrayAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, players);

        /*
        TODO connect to adapter view in activity_wait_for_players and fill with data from WIFI connection
         */
    }
}
