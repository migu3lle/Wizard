package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.Message;

public class TestMessageActivity extends AppCompatActivity implements View.OnClickListener, SalutDataCallback{

    Button btnSend;
    EditText etMessage;

    public Salut network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_message);

        btnSend = findViewById(R.id.btn_Send);
        btnSend.setOnClickListener(this);
        etMessage = findViewById(R.id.et_Message);

        network = GameConfig.getInstance().getSalut();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Send){

            String message = etMessage.getText().toString();
            System.out.println("Want to send: " + message);
            sendMessage(message);
        }
    }

    public void sendMessage(String message){
        Message myMessage = new Message();
        myMessage.description = message;

        network.sendToAllDevices(myMessage, new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });
    }

    @Override
    public void onDataReceived(Object o) {
        Toast.makeText(getApplicationContext(), "New Message received", Toast.LENGTH_SHORT).show();
    }
}
