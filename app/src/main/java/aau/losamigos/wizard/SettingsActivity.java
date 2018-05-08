package aau.losamigos.wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final TextView pwd = findViewById(R.id.Password);
        final TextView pName = findViewById(R.id.Player);
        final EditText Password = findViewById(R.id.PasswordInput);
        final EditText Playername = findViewById(R.id.PlayerNameInput);
        final Button Test = findViewById(R.id.Test);
        Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBrett(Test);
            }
        });
    }

    //temporärer Testbutton um Spielfeld zu testen
    public void showBrett(View view) {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }
}
