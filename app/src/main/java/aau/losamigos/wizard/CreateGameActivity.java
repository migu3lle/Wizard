package aau.losamigos.wizard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import aau.losamigos.wizard.base.GameConfig;

public class CreateGameActivity extends AppCompatActivity {

    EditText textGameName;
    EditText textMinPlayer;
    EditText textMaxPlayer;
    Switch swPw;
    Switch swCheat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        textGameName = findViewById(R.id.editText_gameName);
        textMinPlayer = findViewById(R.id.editText_minPlayer);
        textMaxPlayer = findViewById(R.id.editText_maxPlayer);
        swPw = findViewById(R.id.sw_password);
        swCheat = findViewById(R.id.sw_cheating);

        textMinPlayer.setText("3"); //For Test reasons; TODO: remove
        textMaxPlayer.setText("6"); //For Test reasons; TODO: remove

        configCreateGameButton();
        configBackButton();
    }

    /**
     * Initializes and configures the Button "Create Game"
     * On first action the GameConfig object is generated
     */
    private void configCreateGameButton() {
        Button btnCreateGame = (Button) findViewById(R.id.btn_create);
        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GameConfig gameConfig = GameConfig.getInstance(
                        textGameName.getText().toString(),
                        Integer.parseInt(textMinPlayer.getText().toString()),
                        Integer.parseInt(textMaxPlayer.getText().toString()), swPw.isChecked(), swCheat.isChecked());

                nextActivity();
            }
        });
    }

    /*
    Switches to waitForPlayersActivity
     */
    private void nextActivity() {
        Intent intent = new Intent(this, WaitForPlayersActivity.class);
        startActivity(intent);
    }

    /**
     * Initializes and configures the Button "Back" which switches back to main menu
     */
    private void configBackButton() {
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // startActivity(new Intent(CreateGameActivity.this, MainActivity.class));  //This would call the next-level activity
            }
        });
    }
}



