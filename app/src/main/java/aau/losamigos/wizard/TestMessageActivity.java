package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;

import java.io.IOException;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;

public class TestMessageActivity extends AppCompatActivity implements View.OnClickListener{

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

        DataCallback callback = GameConfig.getInstance().getCallBack();

        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                Toast.makeText(getApplicationContext(), "Message: " + message.description, Toast.LENGTH_SHORT).show();
            }
        });
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


        if(network.isRunningAsHost) {
            network.sendToAllDevices(myMessage, new SalutCallback() {
                @Override
                public void call() {
                    Log.e("WizardApp", "Oh no! The data failed to send.");
                }
            });
        } else {
            network.sendToHost(myMessage,  new SalutCallback() {
                @Override
                public void call() {
                    Log.e("WizardApp", "Oh no! The data failed to send.");
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Salut network = GameConfig.getInstance().getSalut();
        if(network != null) {
            if( network.isRunningAsHost) {
                try {
                    network.stopNetworkService(true);

                } catch (Exception e) {

                }
            } else {
                try {
                    network.unregisterClient(true);
                } catch (Exception e) {

                }
            }
        }
    }
}
