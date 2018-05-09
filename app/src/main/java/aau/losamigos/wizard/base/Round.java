package aau.losamigos.wizard.base;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import aau.losamigos.wizard.TableActivity;
import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;
import aau.losamigos.wizard.rules.Actions;
import aau.losamigos.wizard.rules.JesterRule;
import aau.losamigos.wizard.types.Fractions;
import aau.losamigos.wizard.types.RoundStatus;

/**
 * Created by Andreas.Mairitsch on 18.04.2018.
 */

public class Round{

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

    public void setContext(Context context) {
        this.context = context;
    }

    public Round(GamePlay game, int numberOfCards) {
        this.game = game;
        this.players = game.getPlayers();
        this.playerNumber = game.getPlayerNumber();
        if(numberOfCards != 0) {
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

        List<Player> order = new ArrayList<Player>();
        for (Player player:players) {
            order.add(player);
        }

        generateHands();
    }
    public void startRound(){
        roundCallBack();
        status = RoundStatus.waitingForCard;
        askForCard(order.get(currentPlayer));

    }
    private void checkNextStep(){
        switch(status){
            case waitingForCard:
                break;
            case cardIsPicked:

                sendTableOnAll();
                if(currentPlayer < (players.size()-1)){
                    currentPlayer++;
                    askForCard(players.get(currentPlayer));
                }
                else {
                    status = RoundStatus.tableFull;
                    checkNextStep();
                }
                break;
            case tableFull:
                Player winner = getWinner();
                sendWinnerOnAll(winner);
                /*order = newOrder(winner);
                table.clear();
                currentHandCards--;
                if(currentHandCards>=0)
                    askForCard(order.get(currentPlayer));
                else
                    status = RoundStatus.roundEnded;
                    checkNextStep();*/
                break;
            case roundEnded:
                calcPlayerPoints();
                sendPointsOnAll();
                game.nextRound();
                break;


        }
    }

    private List<Player> newOrder(Player firstPlayer){
        List<Player> newOrder = new ArrayList<Player>();

        newOrder.add(firstPlayer);
        int indexFP = players.indexOf(firstPlayer);
        int afterFP = indexFP+1;
        int beforeFP = 0;
        for (int i = 1; i < playerNumber; i++) {
            if(afterFP<=playerNumber) {
                newOrder.add(players.get(afterFP));
                afterFP++;
            }
            else{
                newOrder.add(players.get(beforeFP));
                beforeFP++;
            }

        }
        currentPlayer=0;
        return newOrder;
    }


    private void sendTableOnAll(){
        Salut network = GameConfig.getInstance().getSalut();

        Message mTableCards = new Message();
        mTableCards.action = Actions.TABLECARDS_ARE_COMING;

        int[] cardIds = new int[table.size()];
        for(int i = 0; i < table.size(); i++) {
            cardIds[i] = table.get(i).getCard().getId();
        }

        mTableCards.cards = cardIds;

        network.sendToAllDevices(mTableCards,new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });

    }
    private Hand lookingForHand(Player player){
        for (Hand hand:hands) {
            if (player.equals(hand.getHandOwner())) {
                return hand;
            }
        }
        return null;
    }

    private void sendWinnerOnAll(Player player){
        Message message = new Message();
        message.action = Actions.PICK_CARD;
        message.sender = player.getName();
        network.sendToDevice(player.getSalutDevice(),message,new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });
        Toast.makeText(context,"Gewonnen hat: " + player.getName(),Toast.LENGTH_LONG).show();
    }
    private void sendPointsOnAll(){
        //TODO Sendet den Gewinner an alle Clients
    }
    private void askForCard(Player player){

        Message mPickCard = new Message();
        mPickCard.action = Actions.PICK_CARD;

        network.sendToDevice(player.getSalutDevice(),mPickCard,new SalutCallback() {
            @Override
            public void call() {
                Log.e("WizardApp", "Oh no! The data failed to send.");
            }
        });
    }
    private void roundCallBack() {
        DataCallback callback = GameConfig.getInstance().getCallBack();
        callback.addCallBackAction(new ICallbackAction() {
            @Override
            public void execute(Message message) {
                if(message == null) {
                    Log.e("CLIENT", "No Message received");
                }
                else if(message.action == Actions.CARD_IS_PICKED) {
                    if(message.cards.length ==1){
                        AbstractCard card =  cardStack.getCardById(message.cards[0]);
                        Player player = getPlayerByName(message.sender);
                        MoveTuple tuple = new MoveTuple(player,card,0);
                        table.add(new MoveTuple(player,card,table.size()+1));
                        lookingForHand(player).removeCard(card);
                        status = RoundStatus.cardIsPicked;
                        checkNextStep();
                    }
                    else{
                        Log.e("CLIENT", "No cards or more than one");
                    }

                }
            }

        });
    }

    private void generateHands(){
        for (Player player:players) {
            hands.add(new Hand(cardStack.getCards(numberOfCards),player));
        }
    }
    private void cleanHands(){
        hands.clear();
    }


    public List<AbstractCard> getPlayerHand(Player player){
        for (Hand hand: hands) {
            if(hand.getHandOwner().equals(player)){
                List<AbstractCard> tableCards = new ArrayList<AbstractCard>();

                for (MoveTuple tuple:table) {
                    tableCards.add(tuple.getCard());
                }
                return hand.getAllowedCards(tableCards); //TODO oder nur die IDs geben
            }

        }
        return null;
    }

    public Player getPlayerByName(String playerName) {
        Player foundPlayer = null;
        for(Player player: players) {
            if(player.getName().equals(playerName)) {
                foundPlayer = player;
                break;
            } else {
                Log.d("PLAYER SEARCH", "no match: " + playerName + ", " + player.getName());
            }
        }
        return foundPlayer;
    }

    public void playCard(String playerName, int cardID){ //throws exception

        MoveTuple playedTupel = new MoveTuple(getPlayerByName(playerName),cardStack.getCardById(cardID),currentPlayer);
        table.add(playedTupel);
        status = RoundStatus.cardIsPicked;
        checkNextStep();


        /*
        for (Hand hand: hands) {
            if(hand.getHandOwner().equals(player)){
                List<AbstractCard> tableCards = new ArrayList<AbstractCard>();

                for (MoveTuple tuple:table) {
                    tableCards.add(tuple.getCard());
                }
                for (AbstractCard card:hand.getAllowedCards(tableCards)) {
                        if (card.getId()==cardID)
                            table.add(new MoveTuple(player,card,table.size()+1));
                        else
                            throw new Exception();
                }
            }

        }
        */

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

}
