package aau.losamigos.wizard.rules;

import java.util.List;

import aau.losamigos.wizard.base.AbstractRule;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;

/**
 * Created by flo on 10.04.2018.
 */

public class ExampleRule extends AbstractRule {
    @Override
    public Player CheckMove(List<MoveTuple> moves) {
        return null;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public int getPoints() {
        return 0;
    }
}
