package aau.losamigos.wizard.base;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.TableActivity;
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
    private List<Player> order;
    private int currentPlayer;
    private int currentHandCards;
    private List<MoveTuple> table;
    private Context context;
    private TableActivity ta;



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

        this.order = new ArrayList<Player>();
        for (Player player : players) {
            this.order.add(player);
        }

        generateHands();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTa(TableActivity ta) {
        this.ta = ta;
    }

    public void startRound() {
        status = RoundStatus.waitingForStiches;
        checkNextStep();
    }

    private void checkNextStep() {
        switch (status) {
            case waitingForStiches:
                if(currentPlayer < order.size()){
                    status = RoundStatus.waitingForStiches;
                    do{
                        askForStiches(order.get(currentPlayer));
                    }while(order.get(currentPlayer).getCalledStiches()<0 || order.get(currentPlayer).getCalledStiches()>order.size());
                    currentPlayer++;
                }
                else{
                    status = RoundStatus.waitingForCard;
                    currentPlayer=0;
                    askForCard(order.get(currentPlayer));
                }
                break;
            case waitingForCard:
                break;
            case cardIsPicked:

                sendTableOnAll();
                if (currentPlayer < (players.size() - 1)) {
                    currentPlayer++;
                    askForCard(players.get(currentPlayer));
                } else {
                    status = RoundStatus.tableFull;
                    checkNextStep();
                }
                break;
            case tableFull:
                Player winner = getWinner();
                sendWinnerOnAll(winner);

                /*
                    5 Sekunde Pause
                */
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                table.clear();

                sendTableOnAll();

                order = newOrder(winner);
                currentHandCards--;
                if(currentHandCards>=0)
                    askForCard(order.get(currentPlayer));
                else
                    status = RoundStatus.roundEnded;
                    checkNextStep();
                break;
            case roundEnded:
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
        Message message = new Message();
        message.action = Actions.AND_THE_WINNER_IS;
        message.sender = player.getName();
        network.sendToAllDevices(message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });
        Toast.makeText(context, "Gewonnen hat: " + player.getName(), Toast.LENGTH_LONG).show();
    }

    private void sendPointsOnAll() {
        Message message = new Message();
        message.action = Actions.YOUR_POINTS;
        for (int i = 0; i < playerNumber ; i++) {
            message.playerPoints[i] = players.get(i).getPoints();
        }

        network.sendToAllDevices(message, new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });

        //TODO Blende Punkte fürn Host ein
    }
    public void returnNumberOfStiches(){
        checkNextStep();
    }

    private void askForCard(Player player) {

        Message mPickCard = new Message();
        mPickCard.action = Actions.PICK_CARD;

        network.sendToDevice(player.getSalutDevice(), mPickCard, new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });
    }
    private void askForStiches(Player player) {

        if(GameConfig.getInstance().isHost()){
            ta.hostStiches();
        }
        else {
            Message mNumberOfTricks = new Message();
            mNumberOfTricks.action = Actions.NUMBER_OF_TRICKS;

            //TODO: Put information about prohibited prediction
            mNumberOfTricks.forbiddenTricks = -1;   //!!! Set to -1 if all numbers are allowed !!!

            network.sendToDevice(player.getSalutDevice(), mNumberOfTricks, new SalutCallback() {
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
                    tableCards.add(tuple.getCard());
                }
                return hand.getAllowedCards(tableCards); //TODO oder nur die IDs geben
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
