package aau.losamigos.wizard.base;


import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.Player;

/**
 * Created by Andreas.Mairitsch on 18.04.2018.
 */

public class Round {

    private List<Player> players; //TODO eventuell in die Gameconfig verschieben
    private CardStack cardStack;
    private List<Hand> hands;
    private int numberOfCards;
    private AbstractCard trump;
    private int playerNumber;

    private ArrayList<AbstractCard> table;

    public Round(ArrayList<Player> players, int numberOfCards) {

        this.players = players;
        this.cardStack = new CardStack();
        this.hands = new ArrayList<Hand>();
        this.numberOfCards = numberOfCards;
        this.trump = cardStack.getTrump();
        this.playerNumber = players.size();

        generateHands();
    }

    private void generateHands(){

        for (Player player:players) {
            hands.add(new Hand(cardStack.getCards(numberOfCards)));
        }

    }
    private void cleanHands(){
        hands.clear();
    }
    public List<AbstractCard> getPlayerHand(Player player){
        for (Hand hand: hands) {
            if(hand.getHandOwner().equals(player))
                return hand.getAllowedCards(table); //TODO oder nur die IDs geben
        }
        return null;
    }

    public void playCard(Player player, int cardID) throws Exception{
        for (Hand hand: hands) {
            if(hand.getHandOwner().equals(player)){
                for (AbstractCard card:hand.getAllowedCards(table)) {
                        if (card.getId()==cardID)
                            table.add(card);
                        else
                            throw new Exception();
                }
            }

        }

    }


}
