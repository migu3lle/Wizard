package aau.losamigos.wizard.rules;

import aau.losamigos.wizard.base.AbstractRule;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.cards.JesterCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 12.04.2018.
 */

/**
 * the first jester to be played always looses
 */
public class JesterRule extends AbstractRule {
    @Override
    public MoveTuple check(MoveTuple currentWinner, MoveTuple nextCard, Fractions trump) {
        MoveTuple winner = currentWinner;

        //if the current card is a jester the next card automatically wins
        if(currentWinner.getExactCard(JesterCard.class) != null) {
            winner = nextCard;
        }

        //otherwise the winner stays the same for now

        return winner;
    }

    @Override
    public int getWeight() {
        return 500;
    }
}
