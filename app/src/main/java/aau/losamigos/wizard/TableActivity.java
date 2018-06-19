package aau.losamigos.wizard;

import android.app.FragmentManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import aau.losamigos.wizard.base.Hand;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.base.Round;
import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;
import aau.losamigos.wizard.rules.Actions;
import aau.losamigos.wizard.rules.Client2HostAction;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public class TableActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    Salut network;
    List<ImageView> cardViews;
    List<ImageView> middleCards;
    ImageView trump;
    int playerCount;
    boolean cheat;
    private List<AbstractCard> oldHand;

    //instance only available for host
    GamePlay game;
    TextView player2, player3, player4, player5, player6;
    CardStack clientCardStack;
    ImageView playerC2, playerC3, playerC4, playerC5, playerC6;

    Button btnPredictTrick;
    Button cheatdetect;
    PredictTrickDialogFragment predictDialog;

    SensorManager sensorManager;
    Sensor accelerometerSensor;
    SensorEventListener accelerometerEventListener;


    HashMap<Integer, AbstractCard> view2CardMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        boolean b = getIntent().getBooleanExtra("CheatEnabled", false);

        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

        player2 = findViewById(R.id.Player2);
        player3 = findViewById(R.id.Player3);
        player4 = findViewById(R.id.Player4);
        player5 = findViewById(R.id.Player5);
        player6 = findViewById(R.id.Player6);

        network = GameConfig.getInstance().getSalut();
        cardViews = new ArrayList<>();
        middleCards = new ArrayList<>();
        view2CardMap = new HashMap<>();
        trump = findViewById(R.id.Trump);
        if (network.isRunningAsHost) {
            defineHostCallBack();
            startGame();
            initGui(game.getPlayers().size(), game.getCountRound());
            setCardsForHost();
        } else {
            clientCardStack = new CardStack();
            defineClientCallBack();
            notifyHost();
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if (b) {
            cheat = false;
            cheating();

        }

    }


    private void initGui(int playerCount, int roundCount) {
        this.playerCount = playerCount;
        Log.e("INIT", "init gui: " + playerCount + ", " + roundCount);

        switch (playerCount) {

            case 2:
                playerC4 = findViewById(R.id.playerC4);
                playerC4.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                break;
            case 3:
                playerC3 = findViewById(R.id.playerC3);
                playerC3.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                playerC5 = findViewById(R.id.playerC5);
                playerC5.setVisibility(View.VISIBLE);
                player5.setVisibility(View.VISIBLE);
                break;
            case 4:
                playerC2 = findViewById(R.id.playerC2);
                playerC2.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                playerC4 = findViewById(R.id.playerC4);
                playerC4.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                playerC6 = findViewById(R.id.playerC6);
                playerC6.setVisibility(View.VISIBLE);
                player6.setVisibility(View.VISIBLE);
                break;
            case 5:
                playerC2 = findViewById(R.id.playerC2);
                playerC2.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                playerC3 = findViewById(R.id.playerC3);
                playerC3.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                playerC5 = findViewById(R.id.playerC5);
                playerC5.setVisibility(View.VISIBLE);
                player5.setVisibility(View.VISIBLE);
                playerC6 = findViewById(R.id.playerC6);
                playerC6.setVisibility(View.VISIBLE);
                player6.setVisibility(View.VISIBLE);
                break;
            case 6:
                playerC4 = findViewById(R.id.playerC4);
                playerC4.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                playerC2 = findViewById(R.id.playerC2);
                playerC2.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                playerC3 = findViewById(R.id.playerC3);
                playerC3.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                playerC5 = findViewById(R.id.playerC5);
                playerC5.setVisibility(View.VISIBLE);
                player5.setVisibility(View.VISIBLE);
                playerC6 = findViewById(R.id.playerC6);
                playerC6.setVisibility(View.VISIBLE);
                player6.setVisibility(View.VISIBLE);
                break;

        }
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

        cheatdetect = findViewById(R.id.cheatdetect);
        cheatdetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.client2HostAction = Client2HostAction.CHEAT_DETECT;
                network.sendToHost(message, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.e("CHEATING NEXT TO ME", "No cheat possible");
                    }
                });

            }
        });

        LinearLayout cardHand = findViewById(R.id.cardHand);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < roundCount; i++) {

            View view = inflater.inflate(R.layout.card, cardHand, false);
            ImageView imageView = view.findViewById(R.id.imageView);

            imageView.setClickable(true);
            imageView.setOnClickListener(this);
            cardHand.addView(view);

            cardViews.add(imageView);
        }


        //TODO: REMOVE - BUTTON WAS JUST FOR TEST REASONS
        btnPredictTrick = findViewById(R.id.btn_predictTrick);
        btnPredictTrick.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   createPredictionPicker(-1);
                                               }
                                           }

        );
    }

    private void cheating() {
        //check if there is a Sensor
        if (accelerometerSensor == null) {

            Toast.makeText(this, "No Cheating possible", Toast.LENGTH_SHORT).show();
            //detect the movement along the z-axis (horizontal skip left/right)
        } else {
            accelerometerEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if (sensorEvent.values[2] > 0.4f) {
                        cardRightPlayer();

                    } else if (sensorEvent.values[2] < -0.4f) {
                        cardLeftPlayer();

                    } else if (sensorEvent.values[2] >= -0.4f && sensorEvent.values[2] <= 0.4f) {

                        //DO NOTHING
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

        }

    }

    private void cardLeftPlayer() {

        Message message = new Message();
        message.sender = network.thisDevice.deviceName;
        message.client2HostAction = Client2HostAction.GET_LEFT_CARDS;
        network.sendToHost(message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("LOOK LEFT","Looking failed");            }
        });

    }


    private void cardRightPlayer() {

        Message message = new Message();
        message.sender = network.thisDevice.deviceName;
        message.client2HostAction = Client2HostAction.GET_RIGHT_CARDS;
    }


    private void startGame() {
        GameConfig gcfg = GameConfig.getInstance();
        game = new GamePlay(gcfg.getPlayers());
        game.startGame(game.getCountRound());
        Round round = game.getRecentRound();
        round.setContext(getApplicationContext());
        gcfg.setCurrentGamePlay(game);
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
                if (message == null) {
                    Log.e("CLIENT", "No Message received");
                } else if (message.action == 0) {
                    Log.e("CLIENT", "No Action defined; Client is blind");
                } else if (message.action == Actions.INITIAL_CARD_GIVING) {
                    if (message.cards != null && message.cards.length > 0 && message.playerCount > 0 && message.roundCount > 0) {
                        initGui(message.playerCount, message.roundCount);
                        List<AbstractCard> cards = getCardsById(message.cards);
                        setCardsToImages(cards);
                    }
                    if (message.trumpCard != 0) {
                        setTrump(clientCardStack.getCardById(message.trumpCard));
                    }
                } else if (message.action == Actions.TABLECARDS_ARE_COMING && message.cards != null) {
                    List<AbstractCard> cards = getCardsById(message.cards);
                    setMiddleCards(cards);
                } else if (message.action == Actions.AND_THE_WINNER_IS) {
                    Toast.makeText(getApplicationContext(), "Gewonnen hat: " + message.sender, Toast.LENGTH_LONG).show();
                } else if (message.action == Actions.PICK_CARD) {
                    //TODO
                } else if (message.action == Actions.NUMBER_OF_TRICKS) {
                    int forbidden = message.forbiddenTricks;
                    createPredictionPicker(forbidden);
                } else if (message.action == Actions.GET_HAND_CARDS) {

                    // TODO: save own hand

                    List<AbstractCard> card = getCardsById(message.getPlayerHand);
                    setCardsToImages(card);


                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            //TODO: create the old owner hand
                        }
                    };
                    handler.postDelayed(r, 3000);
                } else if (message.action == Actions.CHEAT_TRIGGER) {
                    cheat = true;

                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            cheat = false;
                        }
                    };
                    handler.postDelayed(r, 3000);

                }
            }
        });
    }

    private void cheatEnabled(String player) {


    }

    private void defineHostCallBack() {
        final DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                Log.d("HOST CALLBACK", "Received: " + message);
                Round round = game.getRecentRound();
                if (message == null && message.client2HostAction == 0) {
                    Log.e("CLIENT", "No Message received");
                } else if (message.client2HostAction == Client2HostAction.TABLE_ACTIVITY_STARTED) {
                    Player p = round.getPlayerByName(message.sender);
                    if (p != null) {
                        sendCardsToDevice(p);
                    }
                } else if (message.client2HostAction == Client2HostAction.CARD_PLAYED) {
                    int playedCard = message.playedCard;
                    String sender = message.sender;
                    Log.e("CARD RECEIVED", "Card of Client received: " + sender + ", " + playedCard);
                    //TODO: DO SOMETHING WITH THE CARD
                    Round round1 = game.getRecentRound();
                    round1.playCard(sender, playedCard);
                    setMiddleCards(round1.getPlayedCards());

                } else if (message.client2HostAction == Client2HostAction.PREDICTION_SET) {
                    int tricksPrediction = message.predictedTricks;
                    String sender = message.sender;
                    Toast.makeText(getApplicationContext(), "Prediction from " + sender + ": " + tricksPrediction, Toast.LENGTH_SHORT).show();

                    //Write prediction to Player object
                    Player[] players = GameConfig.getInstance().getPlayers();
                    for (Player player : players) {
                        if (player.getSalutDeviceName().equals(sender)) {
                            player.setCalledStiches(tricksPrediction);
                            break;
                        }
                    }
                } else if (message.client2HostAction == Client2HostAction.PLAYERSTATES_REQUESTED) {
                    Player p = round.getPlayerByName(message.sender);
                    if (p != null) {
                        Message m1 = new Message();
                        message.playerStates = game.getPlayerRoundStates();
                        network.sendToDevice(p.getSalutDevice(), m1, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.e("SEND PLAYER STATES", "failed to send player states");
                            }
                        });
                    }

                } else if (message.client2HostAction == Client2HostAction.GET_LEFT_CARDS) {
                    Player p = round.getPlayerByName(message.sender);
                    Player s = null;

                    Player[] players = GameConfig.getInstance().getPlayers();
                    //check the left neighbor of cheater
                    for (int i = 0; i < game.getPlayers().size(); i++) {
                        if (players[i] == p) {

                            //if neighbor is the first player
                            if ((i + 1) > game.getPlayers().size()) {
                                s = players[1];

                                //each other
                            } else {
                                s = players[i + 1];
                            }
                        }
                    }
                    // actual cards of the left sided player
                    List<AbstractCard> hand = round.getPlayerHand(s);
                    Message ms1 = new Message();
                    ms1.action = Actions.GET_HAND_CARDS;
                    int[] cards = new int[round.getPlayerHand(s).size()];
                    for (int i = 0; i < cards.length; i++) {
                        cards[i] = hand.get(i).getId();
                    }
                    ms1.getPlayerHand = cards;

                    Message ms2 = new Message();
                    ms2.action = Actions.CHEAT_TRIGGER;
                    ms2.cheatPlayer = p.getName();
                    //activate cheat detection for the cheated player
                    network.sendToDevice(s.getSalutDevice(), ms2, new SalutCallback() {
                        @Override
                        public void call() {
                            Log.e("DETECT CHEATING", "cheat detection don´t trigger");
                        }
                    });
                    //send hand of the left player to the cheater
                    network.sendToDevice(p.getSalutDevice(), ms1, new SalutCallback() {
                        @Override
                        public void call() {
                            Log.e("SEND LEFT CARDS", "cards sending failed");
                        }
                    });
                } else if (message.client2HostAction == Client2HostAction.CHEAT_DETECT) {

                }
            }

        });
    }

    private List<AbstractCard> getCardsById(int[] cardIds) {
        List<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < cardIds.length; i++) {
            cards.add(clientCardStack.getCardById(cardIds[i]));
        }
        return cards;
    }

    public void resetGame(View view) {
        setContentView(R.layout.activity_table);
    }

    @Override
    public void onClick(View view) {

        if (view instanceof ImageView) {
            ImageView imgView = (ImageView) view;

            //card is not visible so do nothing
            if (imgView.getDrawable() == null) {
                return;
            }

            imgView.setImageDrawable(null);

            AbstractCard clickedCard = view2CardMap.get(view.getId());
            Message message = new Message();
            message.client2HostAction = Client2HostAction.CARD_PLAYED;
            message.playedCard = clickedCard.getId();
            message.sender = network.thisDevice.deviceName;

            if (!network.isRunningAsHost) {
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
                round.playCard(network.thisDevice.deviceName, clickedCard.getId());
                List<AbstractCard> playedCards = round.getPlayedCards();
                setMiddleCards(playedCards);
            }
        }
    }

    public void onClickBdw(View view) {
        Intent intent = new Intent(this, ScoreTableActivity.class);
        intent.putExtra("PLAYER_COUNT", this.playerCount);
        startActivity(intent);
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
        message.playerCount = game.getPlayers().size();
        message.roundCount = game.getCountRound();
        for (int i = 0; i < cards.size(); i++) {
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
        for (int i = 0; i < players.length; i++) {
            final Player player = players[i];
            SalutDevice playerDevice = player.getSalutDevice();
            //then we are host because only host calls this method
            if (playerDevice == network.thisDevice) {
                List<AbstractCard> cards = round.getPlayerHand(player);
                setCardsToImages(cards);
                break;
            }

        }
    }

    private void setCardsToImages(List<AbstractCard> cards) {
        if (cards == null || cards.size() == 0)
            return;
        int maxIteration = cardViews.size();
        if (cards.size() < maxIteration) {
            maxIteration = cards.size();
        }

        for (int i = 0; i < maxIteration; i++) {
            ImageView img = cardViews.get(i);
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
        if (cards.size() < maxIterations)
            maxIterations = cards.size();

        for (int i = 0; i < maxIterations; i++) {
            ImageView img = middleCards.get(i);
            AbstractCard card = cards.get(i);
            img.setImageResource(card.getResourceId());
        }
    }

    private void resetMiddleCards() {
        for (ImageView img : middleCards) {
            img.setImageDrawable(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Salut network = GameConfig.getInstance().getSalut();
        if (network != null) {
            if (network.isRunningAsHost) {
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
    private void createPredictionPicker(int forbiddenTricks) {

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

    /**
     * Fired if predicted number of tricks has been selected in NumberPicker Dialog
     * Value can be retrieved by NumberPicker.getValue()
     *
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
    private void sendNumberOfTricksToHost(int prediction) {
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
