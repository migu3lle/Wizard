package aau.losamigos.wizard.rules;

import java.util.List;

import aau.losamigos.wizard.base.AbstractRule;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.cards.WizardCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

/**
 * the first wizard to be played always wins
 */
public class WizardRule extends AbstractRule {

    @Override
    public MoveTuple check(MoveTuple currentWinner, MoveTuple nextCard, Fractions trump) {
        MoveTuple winner = currentWinner;
        //if the current winner is a wizard he wins automatically
        if(currentWinner.getExactCard(WizardCard.class) == null) {
            //if the current winner is no wizard but the next card is, the next card wins
            if(nextCard.getExactCard(WizardCard.class) != null) {
                winner = nextCard;
            }
            //otherwise the winner stays the same
        }
        return winner;
    }

    @Override
    public int getWeight() {
        return 1000;
    }
}
