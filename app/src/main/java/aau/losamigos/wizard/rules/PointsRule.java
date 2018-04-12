package aau.losamigos.wizard.rules;

import aau.losamigos.wizard.base.AbstractRule;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 12.04.2018.
 */

/**
 * if both cards have the same fraction the one with the higher value wins
 * if the do not have the same fraction the winner stays the same as before
 */
public class PointsRule extends AbstractRule {
    @Override
    public MoveTuple check(MoveTuple currentWinner, MoveTuple nextCard, Fractions trump) {
        MoveTuple winner = currentWinner;

        FractionCard currentFractionCard = currentWinner.getExactCard(FractionCard.class);
        FractionCard nextFractionCard = nextCard.getExactCard(FractionCard.class);

        if(currentFractionCard != null && nextFractionCard != null) {
            //values need only be checked if the fractions are the same
            //if fractions differ the winner stays the same
            if(currentFractionCard.getFraction() == nextFractionCard.getFraction()) {

                //if the value of the next card is higher than the value if the current card, the next card wins
                //otherwise the winner stays the same
                if(currentFractionCard.getValue() < nextFractionCard.getValue()) {
                    winner = nextCard;
                }
            }
        }

        return winner;
    }

    @Override
    public int getWeight() {
        return 50;
    }
}
