package aau.losamigos.wizard.base;

import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

public abstract class AbstractRule {

    /**
     * a method that checks the current winning card against the next card in the order
     * and determines which of the card wins according to this particular rule
     * @param currentWinner the current winning card
     * @param nextCard the next card in the row
     * @param trump the trump in this stich round
     * @return
     * the winner according to that rule
     */
    public abstract MoveTuple check(MoveTuple currentWinner, MoveTuple nextCard, Fractions trump);

    /**
     * a method that determines how important that rule is and when it will be checked
     * by the rule engine
     * @return
     * the weight of the rule
     */
    public abstract int getWeight();


    /**
     * compares the weight (importance) of 2 rules desc
     * @param other the other rule to compare with
     * @return -1, 0, 1
     */
    public int compareToReverse(AbstractRule other) {
        if(this.getWeight() < other.getWeight()) {
            return 1;
        } else if (this.getWeight() > other.getWeight()) {
            return -1;
        }
        return 0;
    }
}
