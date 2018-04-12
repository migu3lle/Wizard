package aau.losamigos.wizard.base;

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

    /**
     * method to get the exact derivation of AbstractCard
     * returns null if the card is not of the given type
     * @param clazz the class containing the type we want to get
     * @param <T> the specific type we want to get
     * @return the specific type if it matches or null if it does not match
     */
    public <T extends AbstractCard> T getExact(Class<T> clazz) {
        T card = null;
        if(clazz.isInstance(this)) {
            card = (T)this;
        }
        return card;
    }
}
