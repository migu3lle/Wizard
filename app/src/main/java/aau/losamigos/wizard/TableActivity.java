package aau.losamigos.wizard;

import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;

import java.util.ArrayList;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.GamePlay;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;

public class TableActivity extends AppCompatActivity implements View.OnClickListener{
    Salut network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

        final ImageView playCard1 = findViewById(R.id.PCard1);
        playCard1.setOnClickListener(this);

        final ImageView playCard2 = findViewById(R.id.PCard2);
        playCard2.setOnClickListener(this);

        final ImageView playCard3 = findViewById(R.id.PCard3);
        playCard3.setOnClickListener(this);

        final ImageView playCard4 = findViewById(R.id.PCard4);
        playCard4.setOnClickListener(this);

        final ImageView playCard5 = findViewById(R.id.PCard5);
        playCard5.setOnClickListener(this);


        network = GameConfig.getInstance().getSalut();

        DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                Toast.makeText(getApplicationContext(), "Message: " + message.description, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void startGame(){
        GameConfig gcfg = GameConfig.getInstance();
        GamePlay  game = new GamePlay(gcfg.getPlayers());
        game.startGame();
    }



    public void resetGame(View view) {
        setContentView(R.layout.activity_table);
    }

    @Override
    public void onClick(View view) {
        String message = String.valueOf(view.getId());
        System.out.println("Want to send: " + message);
        sendMessage(message);
    }


    private void sendMessage(String message) {

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
