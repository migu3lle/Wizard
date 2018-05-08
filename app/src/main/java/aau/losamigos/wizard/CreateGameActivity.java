package aau.losamigos.wizard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import aau.losamigos.wizard.base.GameConfig;

public class CreateGameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText textGameName;
    EditText textMinPlayer;
    EditText textMaxPlayer;
    Switch swPw;
    Switch swCheat;

    Button btnCreateGame;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        textGameName = findViewById(R.id.editText_gameName);
        textMinPlayer = findViewById(R.id.editText_minPlayer);
        textMaxPlayer = findViewById(R.id.editText_maxPlayer);
        swPw = findViewById(R.id.sw_password);
        swCheat = findViewById(R.id.sw_cheating);

        //Initialize buttons
        btnCreateGame = findViewById(R.id.btn_create);
        btnCreateGame.setOnClickListener(this);
        btnBack = findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(this);


        textMinPlayer.setText("2"); //For Test reasons; TODO: remove
        textMaxPlayer.setText("6"); //For Test reasons; TODO: remove

    }

    /*
    Handle Button events on this Activity
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_create){

            //On first action the GameConfig object is generated
            GameConfig gameConfig = GameConfig.getInstance(
                    textGameName.getText().toString(),
                    Integer.parseInt(textMinPlayer.getText().toString()),
                    Integer.parseInt(textMaxPlayer.getText().toString()), swPw.isChecked(), swCheat.isChecked());

            nextActivity();
        }
        if(v.getId() == R.id.btn_Back){
            //Move back to main menu
            finish();
        }
    }

    /*
    Switches to waitForPlayersActivity
     */
    private void nextActivity() {
        Intent intent = new Intent(this, WaitForPlayersActivity.class);
        startActivity(intent);
    }
}



