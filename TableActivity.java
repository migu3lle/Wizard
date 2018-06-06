package aau.losamigos.wizard;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.GamePlay;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.base.Round;
import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;
import aau.losamigos.wizard.rules.Actions;
import aau.losamigos.wizard.rules.Client2HostAction;

public class TableActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener{
    Salut network;
    List<ImageView> cardViews;
    List<ImageView> middleCards;
    ImageView trump;
    GamePlay game;
    TextView player2, player3, player4, player5, player6;
    CardStack clientCardStack;
    ImageView playerC2,playerC3,playerC4,playerC5,playerC6;

    Button btnPredictTrick;
    PredictTrickDialogFragment predictDialog;

    HashMap<Integer, AbstractCard> view2CardMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        player2 =findViewById(R.id.Player2);
        player3 =findViewById(R.id.Player3);
        player4 =findViewById(R.id.Player4);
        player5 =findViewById(R.id.Player5);
        player6 =findViewById(R.id.Player6);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

        initGui();


        network = GameConfig.getInstance().getSalut();

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
        middleCards = new ArrayList<>();
        switch (game.getPlayers().size()){

            case 2: playerC4 = findViewById(R.id.playerC4);
                playerC4.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                break;
            case 3:playerC3= findViewById(R.id.playerC3);
                playerC3.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                playerC5= findViewById(R.id.playerC5);
                playerC5.setVisibility(View.VISIBLE);
                player5.setVisibility(View.VISIBLE);
                break;
            case 4: playerC2=findViewById(R.id.playerC2);
                playerC2.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                playerC4= findViewById(R.id.playerC4);
                playerC4.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                playerC6= findViewById(R.id.playerC6);
                playerC6.setVisibility(View.VISIBLE);
                player6.setVisibility(View.VISIBLE);
                break;
            case 5:
                playerC2= findViewById(R.id.playerC2);
                playerC2.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                playerC3= findViewById(R.id.playerC3);
                playerC3.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                playerC5= findViewById(R.id.playerC5);
                playerC5.setVisibility(View.VISIBLE);
                player5.setVisibility(View.VISIBLE);
                playerC6= findViewById(R.id.playerC6);
                playerC6.setVisibility(View.VISIBLE);
                player6.setVisibility(View.VISIBLE);
                break;
            case 6:
                playerC4= findViewById(R.id.playerC4);
                playerC4.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                playerC2= findViewById(R.id.playerC2);
                playerC2.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                playerC3= findViewById(R.id.playerC3);
                playerC3.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                playerC5= findViewById(R.id.playerC5);
                playerC5.setVisibility(View.VISIBLE);
                player5.setVisibility(View.VISIBLE);
                playerC6= findViewById(R.id.playerC6);
                playerC6.setVisibility(View.VISIBLE);
                player6.setVisibility(View.VISIBLE);
                break;

        }

        LinearLayout cardHand = findViewById(R.id.cardHand);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < game.getCountRound(); i++) {

            View view = inflater.inflate(R.layout.card,cardHand,false);
            ImageView imageView = view.findViewById(R.id.imageView);

            imageView.setClickable(true);



            // TODO: get wright image of the drawable for each card
            //AbstractCard cardIds=clientCardStack.getCardById(i);
            //String cid = cardIds.toString();
            //imageView.setImageResource(R.drawable.cid);


            cardHand.addView(view);
        }
        trump = findViewById(R.id.Trump);




        //TODO: REMOVE - BUTTON WAS JUST FOR TEST REASONS
        btnPredictTrick = findViewById(R.id.btn_predictTrick);
        btnPredictTrick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createPredictionPicker(-1);
            }
        });
    }

    private void startGame(){
        GameConfig gcfg = GameConfig.getInstance();
        game = new GamePlay(gcfg.getPlayers());
        Round round =  game.getRecentRound();
        game.startGame(game.getCountRound());

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
                   else if(message.action == Actions.NUMBER_OF_TRICKS){
                       int forbidden = message.forbiddenTricks;
                       createPredictionPicker(forbidden);
                   }
            }
        });
    }

    private void defineHostCallBack() {
        DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                Log.d("HOST CALLBACK", "Received: " + message);
                Round round = game.getRecentRound();
                if(message == null && message.client2HostAction == 0) {
                    Log.e("CLIENT", "No Message received");
                }

                else if(message.client2HostAction == Client2HostAction.TABLE_ACTIVITY_STARTED) {
                    Player p = round.getPlayerByName(message.sender);
                    if(p != null) {
                        sendCardsToDevice(p);
                    }
                }

                else if(message.client2HostAction == Client2HostAction.CARD_PLAYED) {
                    int playedCard = message.playedCard;
                    String sender = message.sender;
                    Log.e("CARD RECEIVED", "Card of Client received: " +sender + ", " + playedCard);
                    //TODO: DO SOMETHING WITH THE CARD
                    Round round1 = game.getRecentRound();
                    round1.playCard(sender,playedCard);
                    setMiddleCards(round1.getPlayedCards());
                }

                else if (message.client2HostAction == Client2HostAction.PREDICTION_SET){
                    int tricksPrediction = message.predictedTricks;
                    String sender = message.sender;
                    Toast.makeText(getApplicationContext(), "Prediction from " + sender + ": " + tricksPrediction, Toast.LENGTH_SHORT).show();

                    //Write prediction to Player object
                    Player[] players = GameConfig.getInstance().getPlayers();
                    for (Player player : players) {
                        if(player.getSalutDeviceName().equals(sender)){
                            player.setCalledStiches(tricksPrediction);
                            break;
                        }
                    }
                }
            }

        });
    }

    private List<AbstractCard> getCardsById(int[] cardIds) {
        List<AbstractCard> cards = new ArrayList<>();
        for(int i = 0; i < cardIds.length; i++) {
            cards.add(clientCardStack.getCardById(cardIds[i]));
        }
        return cards;
    }

    public void resetGame(View view) {
        setContentView(R.layout.activity_table);
    }

    @Override
    public void onClick(View view) {

        if(view instanceof ImageView) {
            ImageView imgView = (ImageView) view;

            //card is not visible so do nothing
            if(imgView.getDrawable() == null) {
                return;
            }

            imgView.setImageDrawable(null);

            AbstractCard clickedCard = view2CardMap.get(view.getId());
            Message message = new Message();
            message.client2HostAction = Client2HostAction.CARD_PLAYED;
            message.playedCard = clickedCard.getId();
            message.sender = network.thisDevice.deviceName;

            if(!network.isRunningAsHost) {
                network.sendToHost(message, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.e("CARD PLAY", "Client failed to play card");
                    }
                });
            } else {
                Log.e("CARD PLAYED", "Host played card: " + clickedCard.getId());
                //TODO: do something if the host played the card as well
                Round round = game.getRecentRound();
                round.playCard(network.thisDevice.deviceName,clickedCard.getId());
                List<AbstractCard> playedCards = round.getPlayedCards();
                setMiddleCards(playedCards);
            }
        }
    }

    private void sendCardsToDevice(Player player) {
        Round round = game.getRecentRound();
        List<AbstractCard> cards = round.getPlayerHand(player);
        SalutDevice playerDevice = player.getSalutDevice();
        //then we are host because only host calls this method
        Message message = new Message();
        message.action = Actions.INITIAL_CARD_GIVING;
        message.cards = new int[cards.size()];
        message.trumpCard = round.getTrump().getId();
        for(int i = 0; i < cards.size(); i++) {
            message.cards[i] = cards.get(i).getId();
        }
        Log.d("SEND CARDS", message.toString());
        network.sendToDevice(playerDevice, message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("HOST GIVE CARD", "card giving failed for device: ");
            }
        });
    }

    private void setCardsForHost() {
        Round round = game.getRecentRound();
        setTrump(round.getTrump());
        Player[] players = GameConfig.getInstance().getPlayers();
        for(int i = 0; i < players.length; i++) {
            final Player player = players[i];
            SalutDevice playerDevice = player.getSalutDevice();
            //then we are host because only host calls this method
            if(playerDevice == network.thisDevice) {
                List<AbstractCard> cards = round.getPlayerHand(player);
                setCardsToImages(cards);
                break;
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
            view2CardMap.put(img.getId(), card);
            img.setImageResource(card.getResourceId());
        }
    }

    private void setTrump(AbstractCard card) {
        trump.setImageResource(card.getResourceId());
    }

    private void setMiddleCards(List<AbstractCard> cards) {
        resetMiddleCards();

        int maxIterations = middleCards.size();
        if(cards.size() < maxIterations)
            maxIterations = cards.size();

        for(int i =0; i < maxIterations; i++) {
            ImageView img = middleCards.get(i);
            AbstractCard card = cards.get(i);
            img.setImageResource(card.getResourceId());
        }
    }

    private void resetMiddleCards() {
        for(ImageView img: middleCards) {
            img.setImageDrawable(null);
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

    /**
     * Prediction Picker
     * Creates NumberPicker Dialog
     * The selected number is returned in onValueChange() method below
     */
    private void createPredictionPicker(int forbiddenTricks){

        Bundle bundle = new Bundle();
        bundle.putInt("forbiddenTricks", forbiddenTricks);

        FragmentManager manager = getFragmentManager();
        manager.findFragmentByTag("fragment_prediction");
        predictDialog = new PredictTrickDialogFragment();
        //Add bundle information (forbidden tricks) to DialogFragment
        predictDialog.setArguments(bundle);
        predictDialog.setValueChangeListener(this);
        predictDialog.show(manager, "fragment_prediction");
    }

    /** Fired if predicted number of tricks has been selected in NumberPicker Dialog
     *  Value can be retrieved by NumberPicker.getValue()
     * @param picker
     * @param oldVal
     * @param newVal
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int prediction = Integer.parseInt(picker.getDisplayedValues()[picker.getValue()]);
        sendNumberOfTricksToHost(prediction);
    }

    /**
     * Sends back the number of predicted tricks to host
     */
    private void sendNumberOfTricksToHost(int prediction){
        Message message = new Message();
        message.client2HostAction = Client2HostAction.PREDICTION_SET;
        message.sender = network.thisDevice.deviceName;
        message.predictedTricks = prediction;
        Log.d("SEND NUMBER OF TRICKS", message.toString());
        network.sendToHost(message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("CLIENT", "Notification of Host failed");
            }
        });
    }

}
