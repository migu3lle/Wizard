package aau.losamigos.wizard.base;

import java.util.List;

import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;

/**
 * Created by flo on 10.04.2018.
 */

public abstract class AbstractRule {

    /*
    Checks the moves and returns the player that won the move
    If the rule does not apply (for expamle the rules checks the trump and there was no trump played)
    the rules returns null
     */
    public abstract Player CheckMove(List<MoveTuple> moves);

    public abstract int getWeight();

    public abstract int getPoints();

    public int compareTo(AbstractRule other) {
        if(this.getWeight() > other.getWeight()) {
            return 1;
        } else if (this.getWeight() < other.getWeight()) {
            return -1;
        }
        return 0;
    }

    public int compareToReverse(AbstractRule other) {
        if(this.getWeight() < other.getWeight()) {
            return 1;
        } else if (this.getWeight() > other.getWeight()) {
            return -1;
        }
        return 0;
    }
}
