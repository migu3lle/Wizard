package aau.losamigos.wizard.base;


import com.peak.salut.Salut;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import aau.losamigos.wizard.TableActivity;
import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.rules.JesterRule;
import aau.losamigos.wizard.types.Fractions;

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

    private int startNumber;
    private RuleEngine ruleEngine;


    private List<MoveTuple> table;

    public Round(List<Player> players, int numberOfCards) {
        this.players = players;
        this.playerNumber = players.size();
        this.cardStack = new CardStack();
        this.hands = new ArrayList<Hand>();
        this.table = new ArrayList<MoveTuple>();
        this.numberOfCards = numberOfCards;
        this.trump = cardStack.getTrump();
        this.startNumber = 0;
        this.ruleEngine = RuleEngine.getInstance();

        generateHands();
    }

    public void startRound(){

        List<Player> order = new ArrayList<Player>();
        for (Player player:players) {
            order.add(player);
        }
        for (int i = 0; i < numberOfCards; i++) {

            for (Player player:order) {
                AbstractCard card = askForCard(player);
                MoveTuple tuple = new MoveTuple(player,card,0);
                table.add(new MoveTuple(player,card,table.size()+1));
                showCardonTable(tuple);
                lookingForHand(player).removeCard(card);
            }
            //Gewinner des Spielzuges ermitteln und an alle Senden
            Player winner = getWinner();
            sendWinnerOnAll(winner);

            //neue Reihenfolge ermitteln und Tisch leeren
            order = newOrder(winner);
            table.clear();
        }
        calcPlayerPoints();
    }

    private List<Player> newOrder(Player firstPlayer){
        ArrayList<Player> newOrder = new ArrayList<Player>();

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
        return newOrder;
    }


    private void showCardonTable(MoveTuple tuple){
        //TODO senden Karte an alle Clients um diese auf den Tisch anzuzeigen

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
        //TODO Sendet den Gewinner an alle Clients
    }
    private AbstractCard askForCard(Player player){
        Salut network = GameConfig.getInstance().getSalut();

        AbstractCard card = network.sendToDevice();

        player.

        network.
        return null;
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

    public void playCard(Player player, int cardID) throws Exception{
        //TODO Solange Handkarten spielen bis jeder Spieler eine Karte gespielt hat
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

    }

    public Player getWinner(){

        FractionCard trumpCard = trump.getExact(FractionCard.class);
        Fractions trumpFraction = null;
        if (trumpCard != null) {
            trumpFraction = trumpCard.getFraction();
        }

        return ruleEngine.processRound(table, trumpFraction);
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


}
