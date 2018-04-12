package aau.losamigos.wizard.rules;

import aau.losamigos.wizard.base.AbstractRule;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 12.04.2018.
 */

/**
 * the trump card wins; if both cards are trump or none of them is the winner stays the same
 */
public class TrumpCardRule extends AbstractRule {
    @Override
    public MoveTuple check(MoveTuple currentWinner, MoveTuple nextCard, Fractions trump) {
        MoveTuple winner = currentWinner;

        if(trump != null) {
            FractionCard currentFractionCard = currentWinner.getExactCard(FractionCard.class);
            FractionCard nextFractionCard = nextCard.getExactCard(FractionCard.class);

            if(currentFractionCard != null && nextFractionCard != null) {

                //next card is trump and current one is not so next one wins
                if(currentFractionCard.getFraction() != trump && nextFractionCard.getFraction() == trump) {
                    winner = nextCard;
                }

                //otherwise if both or none of both cards are trump the winner stays the same
                //points will be evaluated by points rule
            }
        }

        return winner;
    }

    @Override
    public int getWeight() {
        return 100;
    }
}
