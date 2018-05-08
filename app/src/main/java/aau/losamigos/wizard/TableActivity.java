package aau.losamigos.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.GamePlay;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.base.Round;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;
import aau.losamigos.wizard.rules.Actions;

public class TableActivity extends AppCompatActivity implements View.OnClickListener{
    Salut network;

    List<ImageView> cardViews;

    GamePlay game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

        cardViews = new ArrayList<>();
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

        cardViews.add(playCard1);
        cardViews.add(playCard2);
        cardViews.add(playCard3);
        cardViews.add(playCard4);
        cardViews.add(playCard5);

        network = GameConfig.getInstance().getSalut();

        if(network.isRunningAsHost) {
            defineHostCallBack();
        } else {
            definceClientCallBack();
        }

        startGame();
        
        sendCardsToDevices();
    }
    private void startGame(){
        GameConfig gcfg = GameConfig.getInstance();
        game = new GamePlay(gcfg.getPlayers());
        game.startGame();
    }

    private void definceClientCallBack() {
        DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                   if(message == null) {
                        Log.e("CLIENT", "No Message received");
                   } else if(message.action == null) {
                       Log.e("CLIENT", "No Action defined; Client is blind");
                   }
                   else if(message.action == Actions.INITIAL_CARD_GIVING) {
                       if(message.cards != null && message.cards.size() > 0) {
                           setCardsToImages(message.cards);
                       }
                   }
            }

        });
    }

    private void defineHostCallBack() {
        DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                if(message == null) {
                    Log.e("CLIENT", "No Message received");
                }
                //TODO: do some host stuff
            }

        });
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

    private void sendCardsToDevices() {
        Round round = game.getRecentRound();
        Player[] players = GameConfig.getInstance().getPlayers();
        for(int i = 0; i < players.length; i++) {
            final Player player = players[i];
            List<AbstractCard> cards = round.getPlayerHand(player);
            SalutDevice playerDevice = player.getSalutDevice();
            //then we are host because only host calls this method
            if(playerDevice == network.thisDevice) {
                setCardsToImages(cards);
            } else {
                Message message = new Message();
                message.action = Actions.INITIAL_CARD_GIVING;
                message.cards = cards;
                network.sendToDevice(playerDevice, message, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.e("HOST GIVE CARD","card giving failed for device: " + player.getName());
                    }
                });
            }
        }
    }

    private void setCardsToImages(List<AbstractCard> cards) {
        if(cards == null || cards.size() == 0)
            return;
        int maxIteration = 5;
        if(cards.size() < maxIteration) {
            maxIteration = cards.size();
        }

        for(int i = 0; i < maxIteration; i++) {
            ImageView img =  cardViews.get(i);
            AbstractCard card = cards.get(i);
            img.setImageResource(card.getResourceId());
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
