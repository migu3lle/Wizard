package aau.losamigos.wizard.base;

import java.util.ArrayList;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by Andreas.Mairitsch on 18.04.2018.
 */

public class Round {

    private ArrayList<Player> players; //TODO eventuell in die Gameconfig verschieben
    private ArrayList<Hand> hands;
    private int rounds;
    private int trump;
    private int playerNumber;

    private ArrayList<AbstractCard> table;


    private void generateHands(){

        ArrayList<AbstractCard> cards = getCards(rounds*playerNumber);

        int index = 0;
        for (Player player:players) {
            ArrayList<AbstractCard> newHandcards = (ArrayList<AbstractCard>) cards.subList(index,index+rounds);
            index = index + rounds;
            hands.add(new Hand(newHandcards));
        }




    }



}
