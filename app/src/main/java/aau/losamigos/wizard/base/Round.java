package aau.losamigos.wizard.base;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.rules.Actions;
import aau.losamigos.wizard.types.Fractions;
import aau.losamigos.wizard.types.RoundStatus;

/**
 * Created by Andreas.Mairitsch on 18.04.2018.
 */

public class Round {

    private List<Player> players;
    private int playerNumber;
    private GamePlay game;
    private CardStack cardStack;
    private List<Hand> hands;
    private int numberOfCards;
    private AbstractCard trump;
    private RoundStatus status;
    private Salut network;
    private RuleEngine ruleEngine;
    private static List<Player> order;
    private int currentPlayer;
    private int currentHandCards;
    private List<MoveTuple> table;
    private static Context context;
    //instance of gameActivity to be able to clear table of host
    private static IGameActivity gameActivity;
    private int initialPredictionCount = 0;



    public Round(GamePlay game, int numberOfCards) {
        this.game = game;
        this.players = game.getPlayers();
        this.playerNumber = game.getPlayerNumber();
        if (numberOfCards != 0) {
            this.numberOfCards = numberOfCards;
        } else {
            this.numberOfCards = game.getCountRound();
        }

        this.cardStack = game.getCardStack();

        this.hands = new ArrayList<Hand>();
        this.table = new ArrayList<MoveTuple>();

        this.trump = cardStack.getTrump();
        this.ruleEngine = RuleEngine.getInstance();
        this.status = RoundStatus.start;
        this.network = GameConfig.getInstance().getSalut();
        this.currentPlayer = 0;
        this.currentHandCards = this.numberOfCards;

        if(order==null){
            this.order = new ArrayList<>();
            order.addAll(players);
        }


        generateHands();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setGameActivity(IGameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void startRound() {
        for(Player player : players) {
            gameActivity.sendCardsToDevice(player);
        }
        status = RoundStatus.waitingForStiches;
        Log.d("WizardApp", "Changed state to waitingForStiches");
        checkNextStep();
    }

    //Asks for first prediction (when TableActivity onCreate()), beginning with host
    public void askFirstPredictions(){
        //Ask host
        if(initialPredictionCount == 0){
            Log.d("WizardApp", "Ask host: " + GameConfig.getInstance().getPlayers()[0] + " for stiches.");
            askForStiches(GameConfig.getInstance().getPlayers()[initialPredictionCount]);
            initialPredictionCount++;
            currentPlayer++;
        }
        //Ask clients
        else if(initialPredictionCount > 0 && initialPredictionCount < players.size()){
            Log.d("WizardApp", "Ask player: " + initialPredictionCount + " for stiches.");
            askForStiches(GameConfig.getInstance().getPlayers()[initialPredictionCount]);
            initialPredictionCount++;
            currentPlayer++;
        }
        //All players asked, go to next state
        else{
            Log.d("WizardApp", "Initial predictions are done now.");
            gameActivity.setInitialPrediction(false);
            status = RoundStatus.waitingForCard;
            currentPlayer=0;
            checkNextStep();
        }
    }

    private void checkNextStep() {
        switch (status) {
            case waitingForStiches:
                if(currentPlayer < order.size()){
                    status = RoundStatus.waitingForStiches;
                    Log.d("WizardApp", "Now aks for Stiches to player: " + order.get(currentPlayer).getSalutDeviceName() + "(currentPlayer = " + currentPlayer + ")");
                    askForStiches(order.get(currentPlayer));
                    currentPlayer++;
                }
                else{
                    status = RoundStatus.waitingForCard;
                    currentPlayer=0;
                    checkNextStep();
                }
                break;
            case waitingForCard:
                askForCard(order.get(currentPlayer));
                currentPlayer++;
                break;
            case cardIsPicked:

                sendTableOnAll();
                if (currentPlayer < (players.size())) {
                    status = RoundStatus.waitingForCard;
                    checkNextStep();

                } else {
                    status = RoundStatus.tableFull;
                    checkNextStep();
                }
                break;
            case tableFull:
                final Player winner = getWinner();
                //show winner on host
                Toast.makeText(context, "Gewonnen hat: " + winner.getName(), Toast.LENGTH_LONG).show();

                //show winner on clients
                sendWinnerOnAll(winner);

                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        table.clear();

                        gameActivity.clearTable();

                        sendTableOnAll();

                        order = newOrder(winner);
                        currentHandCards--;
                        if(currentHandCards>0)
                        {
                            status = RoundStatus.waitingForCard;
                            checkNextStep();
                        } else {
                            status = RoundStatus.roundEnded;
                            checkNextStep();
                        }
                    }
                };

                handler.postDelayed(r, 3000);

                break;
            case roundEnded:
                Log.d("ROUNDENDED", "round ended");
                calcPlayerPoints();
                sendPointsOnAll();
                game.nextRound();
                break;


        }
    }

    private List<Player> newOrder(Player firstPlayer) {
        List<Player> newOrder = new ArrayList<Player>();

        newOrder.add(firstPlayer);

        int indexFP = players.indexOf(firstPlayer);

        for (int i = indexFP+1; i < playerNumber; i++)
            newOrder.add(players.get(i));

        for (int i = 0; i < indexFP; i++)
            newOrder.add(players.get(i));

        currentPlayer = 0;
        return newOrder;
    }


    private void sendTableOnAll() {
        Salut network = GameConfig.getInstance().getSalut();

        gameActivity.setMiddleCards(getPlayedCards());

        Message mTableCards = new Message();
        mTableCards.action = Actions.TABLECARDS_ARE_COMING;

        int[] cardIds = new int[table.size()];
        for (int i = 0; i < table.size(); i++) {
            cardIds[i] = table.get(i).getCard().getId();
        }

        mTableCards.cards = cardIds;

        network.sendToAllDevices(mTableCards, new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });

    }

    private Hand lookingForHand(Player player) {
        for (Hand hand : hands) {
            if (player.equals(hand.getHandOwner())) {
                return hand;
            }
        }
        return null;
    }

    private void sendWinnerOnAll(Player player) {
        Log.d("HOSTDEVICE", "send winner on all; player: " + player);
        Message message = new Message();
        message.action = Actions.AND_THE_WINNER_IS;
        message.sender = player.getName();
        network.sendToAllDevices(message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });
    }

    private void askForStiches(Player player) {
        Log.d("WizardApp", "askForStiches(): player = " + player.getSalutDeviceName());

        int forbiddenTricks = 0;

        if(currentPlayer==0){
            forbiddenTricks = -1;
        }
        else{
            int predictionCount = 0;
            for (Player p : players) {
                predictionCount += p.getCalledStiches();
            }
            forbiddenTricks = numberOfCards-predictionCount;
        }


        //Compare to Player Array, if host build local prediction picker. If client send message
        if(player.equals(GameConfig.getInstance().getPlayers()[0])){
            Log.d("WizardApp", "Ask for hostStiches()");
            gameActivity.hostStiches(forbiddenTricks);

        }
        else {
            Log.d("WizardApp", "Ask for clientStiches per message to player: " + player.getSalutDeviceName());
            Message mNumberOfTricks = new Message();
            mNumberOfTricks.action = Actions.NUMBER_OF_TRICKS;
            mNumberOfTricks.forbiddenTricks = forbiddenTricks;
            /*
            //In case of asking the last player for prediction, check which number to exclude
            if((currentPlayer == players.size()-1) || (initialPredictionCount == players.size()-1)){
                int predictionCount = 0;
                for (Player p : players) {
                    predictionCount += p.getCalledStiches();
                }
                //Calc the difference to real number of tricks and set this number as not allowed for last prediction
                mNumberOfTricks.forbiddenTricks = numberOfCards-predictionCount;
            }
            else{
                //All numbers allowed
                mNumberOfTricks.forbiddenTricks = -1;
            }
            */

            network.sendToDevice(player.getSalutDevice(), mNumberOfTricks, new SalutCallback() {
                @Override
                public void call() {
                    Log.e("WizardApp", "Oh no! The data failed to send.");
                }
            });
        }
    }

    private void sendPointsOnAll() {
        Log.d("PLAYERPOINTS", "atemting to send points");
        Message message = new Message();
        message.action = Actions.YOUR_POINTS;
        for (int i = 0; i < playerNumber ; i++) {
            Player player = players.get(i);
            SalutDevice device = player.getSalutDevice();
            message.playerPoints = player.getPoints();

            //host
            if(network.thisDevice == device) {
                Toast.makeText(context, "Your Points: "+ message.playerPoints, Toast.LENGTH_LONG).show();
            }
            //client
            else {
                network.sendToDevice(device, message, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.e("POINTS", "failed to send points to device");
                    }
                });
            }
        }
    }


    public void returnNumberOfStiches(){
        checkNextStep();
    }
    private void askForCard(Player player) {

        List<AbstractCard> handCards = getPlayerHand(player);

        List<Integer> allowedHandCardsId = new ArrayList<>();
        for (AbstractCard card:handCards) {
            if(card.isAllowedToPlay())
                allowedHandCardsId.add(card.getId());
        }

        if(player.equals(GameConfig.getInstance().getPlayers()[0])){
            gameActivity.hostPickCard(allowedHandCardsId);
        }
        else {
            int arrayToSend[] = new int[allowedHandCardsId.size()];

            for (int i = 0; i < allowedHandCardsId.size(); i++) {
                arrayToSend[i]=allowedHandCardsId.get(i);
            }

            Message mPickCard = new Message();
            mPickCard.action = Actions.PICK_CARD;
            mPickCard.cardsAllowedToPlay = arrayToSend;


            network.sendToDevice(player.getSalutDevice(), mPickCard, new SalutCallback() {
                @Override
                public void call() {
                    Log.e("WizardApp", "Oh no! The data failed to send.");
                }
            });
        }
    }

    private void generateHands() {
        for (Player player : players) {
            hands.add(new Hand(cardStack.getCards(numberOfCards), player));
        }
    }

    private void cleanHands() {
        hands.clear();
    }


    public List<AbstractCard> getPlayerHand(Player player) {
        for (Hand hand : hands) {
            if (hand.getHandOwner().equals(player)) {
                List<AbstractCard> tableCards = new ArrayList<AbstractCard>();

                for (MoveTuple tuple : table) {
                    Log.d("WizardApp", "Adding card " + tuple.getCard().getId() + " to hand of player: " + hand.getHandOwner().getSalutDeviceName());
                    tableCards.add(tuple.getCard());
                }

                return hand.getAllowedCards(tableCards, trump); //TODO oder nur die IDs geben
            }

        }
        return null;
    }

    /*
    Entfernt gespielte Karte von der Hand des Spielers
     */
    public void removeCard(Player player, AbstractCard card) { //throws exception

        for (Hand hand : hands)
            if (hand.getHandOwner().equals(player))
                hand.removeCard(card);

    }

    /*
    Fügt die gespielte Karte des Spielers der Spielmitte hinzu
     */
    public void playCard(String playerName, int cardID){ //throws exception

        Player player = getPlayerByName(playerName);
        AbstractCard card = cardStack.getCardById(cardID);

        MoveTuple playedTupel = new MoveTuple(player, card, currentPlayer);
        table.add(playedTupel);
        removeCard(player,card);
        status = RoundStatus.cardIsPicked;
        checkNextStep();

    }


    public Player getWinner(){

        FractionCard trumpCard = trump.getExact(FractionCard.class);
        Fractions trumpFraction = null;
        if (trumpCard != null) {
            trumpFraction = trumpCard.getFraction();
        }

        return ruleEngine.processRound(table, trumpFraction);
    }

    public AbstractCard getTrump() {
        return trump;
    }

    private void calcPlayerPoints(){
        for (Player player: players ) {
            int actualStiches = player.getActualStiches();
            int calledStiches = player.getCalledStiches();

            if(actualStiches==calledStiches)
                player.addPoints(20+(player.getActualStiches()*10));
            else if(actualStiches<calledStiches)
                player.addPoints((calledStiches-actualStiches)*(-10));
            else
                player.addPoints((actualStiches-calledStiches)*(-10));
        }
    }

    public List<AbstractCard> getPlayedCards() {
        List<AbstractCard> playedCards = new ArrayList<>();
        for(MoveTuple tuple:table) {
            playedCards.add(tuple.getCard());
        }
        return playedCards;
    }

    /*********************************************************************************************
     Hilfsmethoden
     */
    public Player getPlayerByName(String playerName) {
        Player foundPlayer = null;
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                foundPlayer = player;
                break;
            } else {
                Log.d("PLAYER SEARCH", "no match: " + playerName + ", " + player.getName());
            }
        }
        return foundPlayer;
    }

}