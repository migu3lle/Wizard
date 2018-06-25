package aau.losamigos.wizard;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final TextView pName = findViewById(R.id.Player);
        final EditText Playername = findViewById(R.id.PlayerNameInput);
        final Button Test = findViewById(R.id.Test);
        Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "myName";
                String fileContent = Playername.getText().toString();

                OutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(fileContent.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                saveName(Test);
            }
        });
    }

    //temporärer Testbutton um Spielfeld zu testen
    public void saveName(View view) {

        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }
}
