package aau.losamigos.wizard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.types.Fractions;

public class CreateGameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText textHostName;
    EditText textMinPlayer;
    EditText textMaxPlayer;
    Switch swCheat;

    Button btnCreateGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        textHostName = findViewById(R.id.editText_hostName);
        textMinPlayer = findViewById(R.id.editText_minPlayer);
        textMaxPlayer = findViewById(R.id.editText_maxPlayer);
        swCheat = findViewById(R.id.sw_cheating);

        //Initialize buttons
        btnCreateGame = findViewById(R.id.btn_create);
        btnCreateGame.setOnClickListener(this);



        textMinPlayer.setText("2"); //For Test reasons; TODO: remove
        textMaxPlayer.setText("6"); //For Test reasons; TODO: remove
        textHostName.setText(Fractions.getRandomFraction());
    }

    /*
    Handle Button events on this Activity
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_create){

            //On first action the GameConfig object is generated
            GameConfig gameConfig = GameConfig.getInstance(
                    Integer.parseInt(textMinPlayer.getText().toString()),
                    Integer.parseInt(textMaxPlayer.getText().toString()), swCheat.isChecked());

            nextActivity();
        }

    }

    /*
    Switches to waitForPlayersActivity
     */
    private void nextActivity() {
        Intent intent = new Intent(this, WaitForPlayersActivity.class);
        intent.putExtra("hostPlayerName", textHostName.getText().toString());
        startActivity(intent);
    }
}



