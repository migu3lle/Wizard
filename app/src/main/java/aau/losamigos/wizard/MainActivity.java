package aau.losamigos.wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peak.salut.Salut;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    Button btnCreateGame;
    Button btnLaunchGame;
    Button btnRules;
    Button btnSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Buttons
        btnCreateGame = findViewById(R.id.btn_CreateGame);
        btnCreateGame.setOnClickListener(this);
        btnLaunchGame = findViewById(R.id.btn_LaunchGame);
        btnLaunchGame.setOnClickListener(this);
        btnRules = findViewById(R.id.btn_Rules);
        btnRules.setOnClickListener(this);
        btnSettings = findViewById(R.id.btn_Settings);
        btnSettings.setOnClickListener(this);

    }

    /*
    Handle Button events on this Activity
    */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_CreateGame) {
            createGame(btnCreateGame);
        } else if (v.getId() == R.id.btn_LaunchGame) {
            launchGame(btnLaunchGame);
        } else if (v.getId() == R.id.btn_Rules) {
            showRules(btnRules);
        } else if (v.getId() == R.id.btn_Settings) {
            settings(btnSettings);
        }
    }


    /**
     * Checks if device has WIFI enabled
     *
     * @return true if enabled, false (+Toast) if disabled
     */
    private boolean wifiEnabled() {
        if (!Salut.isWiFiEnabled(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please enable WiFi first.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void createGame(View view) {
        Intent intent = new Intent(this, CreateGameActivity.class);
        //intent.putExtra("SalutServices", salutServices);
        startActivity(intent);
    }

    public void launchGame(View view) {
        Intent intent = new Intent(this, JoinGameActivity.class);
        //intent.putExtra("SalutServices", salutServices);
        startActivity(intent);
    }

    public void showRules(View view) {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void showScoreSheet(View view) {
        EditText tf = findViewById(R.id.player_count_test);
        String playerCount = tf.getText().toString();
        Intent intent = new Intent(this, ScoreTableActivity.class);
        intent.putExtra("PLAYER_COUNT", playerCount);
        startActivity(intent);
    }
}
