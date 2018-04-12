package aau.losamigos.wizard.base;

import java.lang.reflect.ParameterizedType;

import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 12.04.2018.
 */

public class AbstractCard {

    protected int id;

    public AbstractCard(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    /*
    method to get the exact derivation of AbstractCard
    returns null if the card is not of the given type
     */
    public <T extends AbstractCard> T getExact(Class<T> clazz) {
        T card = null;
        if(clazz.isInstance(this)) {
            card = (T)this;
        }
        return card;
    }
}
