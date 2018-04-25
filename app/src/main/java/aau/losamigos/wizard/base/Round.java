package aau.losamigos.wizard.base;

import java.util.AbstractList;
import java.util.ArrayList;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by Andreas.Mairitsch on 18.04.2018.
 */

public class Round {

    private ArrayList<Player> players; //TODO eventuell in die Gameconfig verschieben
    private CardStack cardStack;
    private ArrayList<Hand> hands;
    private int rounds;
    private int trump;
    private int playerNumber;

    private ArrayList<AbstractCard> table;

    public Round(ArrayList<Player> players, int rounds) {

        this.players = players;
        this.cardStack = new CardStack();
        this.hands = new ArrayList<Hand>();
        this.rounds = rounds;
        this.trump = cardStack.getTrump();
        this.playerNumber = players.size();

        generateHands();
    }

    private void generateHands(){

        ArrayList<AbstractCard> cards = cardStack.getCards(rounds*playerNumber);

        int index = 0;
        for (Player player:players) {
            ArrayList<AbstractCard> newHandcards = (ArrayList<AbstractCard>) cards.subList(index,index+rounds);
            index = index + rounds;
            hands.add(new Hand(newHandcards));
        }

    }
    private void cleanHands(){
        hands.clear();
    }
    public AbstractList<AbstractCard> getPlayerHand(Player player){
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
