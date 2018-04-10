package aau.losamigos.wizard.elements;

import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

public class Card {

    //don't really know if an id is needed because the id could as well be value + fraction
    private int id;

    private int value;

    private Fractions fraction;

    public Card(int id, int value, Fractions fraction) {
        this.id = id;
        this.value = value;
        this.fraction = fraction;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Fractions getFraction() {
        return fraction;
    }
}
