package aau.losamigos.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import aau.losamigos.wizard.base.GameConfig;

public class CreateGameActivity extends AppCompatActivity {

    EditText textGameName = findViewById(R.id.editText_gameName);
    EditText textMinPlayer = findViewById(R.id.editText_minPlayer);
    EditText textMaxPlayer = findViewById(R.id.editText_maxPlayer);
    Switch swPw = findViewById(R.id.sw_password);
    Switch swCheat = findViewById(R.id.sw_cheating);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configCreateGameButton();
        configBackButton();
        };


    /**
     * Initializes and configures the Button "Create Game"
     * This action triggers the P2P connection setup and generates the GameConfig object
     */
    private void configCreateGameButton(){
        Button btnCreateGame = (Button) findViewById(R.id.btn_create);
        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createWifiConnection();
                /*
                Create new GameConfig object. If GameConfig stores WIFI connections too, handover in Constructor
                 */
                GameConfig gameConfig = new GameConfig(
                        textGameName.getText().toString(),
                        Integer.parseInt(textMinPlayer.getText().toString()),
                        Integer.parseInt(textMaxPlayer.getText().toString()), swPw.isChecked(), swCheat.isChecked());

            }
        });
    }

    /**
     * Method to trigger P2P connection initialization
     */
    private void createWifiConnection(){
        /*
        TODO Generate WIFI connection here
        */
    }

    /**
     * Initializes and configures the Button "Back" which switches back to main menu
     */
    private void configBackButton(){
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
                // startActivity(new Intent(CreateGameActivity.this, MainActivity.class));  //This would call the next-level activity
            }
        });
    }

}
