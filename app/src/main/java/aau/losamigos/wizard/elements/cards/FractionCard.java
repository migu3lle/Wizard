package aau.losamigos.wizard.elements.cards;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

public class FractionCard extends AbstractCard {

    private int value;

    private Fractions fraction;

    public FractionCard(int id, int value, Fractions fraction, int resourceId) {
        super(id, resourceId);
        this.value = value;
        this.fraction = fraction;
    }

    public int getId() {
        return this.id;
    }

    public int getValue() {
        return this.value;
    }

    public Fractions getFraction() {
        return this.fraction;
    }
}
