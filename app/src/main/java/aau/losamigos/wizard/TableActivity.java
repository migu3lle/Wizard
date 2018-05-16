package aau.losamigos.wizard;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.peak.salut.Salut;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.network.DataCallback;

public class TableActivity extends AppCompatActivity implements View.OnClickListener {

    public Salut network;

    ImageView pCard1;
    ImageView pCard2;
    ImageView pCard3;
    ImageView pCard4;
    ImageView pCard5;

    ImageView playedCard1;

    Drawable img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

        network = GameConfig.getInstance().getSalut();

<<<<<<< HEAD
        if(network.isRunningAsHost) {
            defineHostCallBack();
            startGame();
            setCardsForHost();
        } else {
            clientCardStack = new CardStack();
            defineClientCallBack();
            notifyHost();
        }
    }

    private void initGui() {
        view2CardMap = new HashMap<>();
        cardViews = new ArrayList<>();
        middleCards = new ArrayList<>();
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

        trump = findViewById(R.id.Trump);

        cardViews.add(playCard1);
        cardViews.add(playCard2);
        cardViews.add(playCard3);
        cardViews.add(playCard4);
        cardViews.add(playCard5);

        final ImageView middleCard1 = findViewById(R.id.Middle1);
        final ImageView middleCard2 = findViewById(R.id.Middle2);
        final ImageView middleCard3 = findViewById(R.id.Middle3);
        final ImageView middleCard4 = findViewById(R.id.Middle4);
        final ImageView middleCard5 = findViewById(R.id.Middle5);
        final ImageView middleCard6 = findViewById(R.id.Middle6);

        middleCards.add(middleCard1);
        middleCards.add(middleCard2);
        middleCards.add(middleCard3);
        middleCards.add(middleCard4);
        middleCards.add(middleCard5);
        middleCards.add(middleCard6);

    }

    private void startGame(){
        GameConfig gcfg = GameConfig.getInstance();
        game = new GamePlay(gcfg.getPlayers());
        game.startGame(5);
        Round round =  game.getRecentRound();
        round.setContext(getApplicationContext());
    }

    private void notifyHost() {
        Message message = new Message();
        message.client2HostAction = Client2HostAction.TABLE_ACTIVITY_STARTED;
        message.sender = network.thisDevice.deviceName;
        network.sendToHost(message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("CLIENT", "Notification of Host failed");
            }
        });
    }

    private void defineClientCallBack() {
        final DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                Log.d("CLIENT CALLBACK", "Received: " + message);
                   if(message == null) {
                        Log.e("CLIENT", "No Message received");
                   } else if(message.action == 0) {
                       Log.e("CLIENT", "No Action defined; Client is blind");
                   }
                   else if(message.action == Actions.INITIAL_CARD_GIVING) {
                       if(message.cards != null && message.cards.length > 0) {
                           List<AbstractCard> cards =getCardsById(message.cards);
                           setCardsToImages(cards);
                       }
                       if(message.trumpCard != 0) {
                           setTrump(clientCardStack.getCardById(message.trumpCard));
                       }
                   }
                   else if(message.action == Actions.TABLECARDS_ARE_COMING && message.cards != null) {
                       List<AbstractCard> cards =getCardsById(message.cards);
                       setMiddleCards(cards);
                   }
                   else if(message.action == Actions.AND_THE_WINNER_IS) {
                       Toast.makeText(getApplicationContext(),"Gewonnen hat: " + message.sender,Toast.LENGTH_LONG).show();
                   }
                   else if(message.action == Actions.PICK_CARD) {
                       //TODO
                   }
            }
        });
    }

    private void defineHostCallBack() {
=======
>>>>>>> 3cc8d337a3aa6136ff08d496309728bc18cafc1c
        DataCallback callback = GameConfig.getInstance().getCallBack();
        pCard1 = findViewById(R.id.PCard1);
        pCard2 = findViewById(R.id.PCard2);
        pCard3 = findViewById(R.id.PCard3);
        pCard4 = findViewById(R.id.PCard4);
        pCard5 = findViewById(R.id.PCard5);

        pCard1.setOnClickListener(this);
        pCard2.setOnClickListener(this);
        pCard3.setOnClickListener(this);
        pCard4.setOnClickListener(this);
        pCard5.setOnClickListener(this);

        playedCard1 = findViewById(R.id.playedCard1);

    }

    public void onClick(View v) {
        ImageView card = (ImageView)v;
        if(v.getId() == R.id.PCard1){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard2){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard3){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard4){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard5){
            playCard1(card);
        }




        }

   public void playCard1(ImageView card){
        img = card.getDrawable();
        playedCard1.setImageDrawable(img);
        playedCard1.setVisibility(View.VISIBLE);

   }

    public void resetGame(View view) {
        setContentView(R.layout.activity_table);
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
