package aau.losamigos.wizard.elements;

import aau.losamigos.wizard.base.AbstractCard;

/**
 * Created by flo on 10.04.2018.
 */

//maybe there is a better name for this class
public class MoveTuple {

    private Player player;

    private AbstractCard card;

    private int order;

    public MoveTuple(Player player, AbstractCard card, int order) {
        this.player = player;
        this.card = card;
        this.order = order;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractCard getCard() {
        return card;
    }

    public <T extends  AbstractCard> T getExactCard(Class<T> clazz) {
        return this.card.getExact(clazz);
    }

    public int getOrder() {
        return order;
    }

    public int compareOrder(MoveTuple other) {
        if(this.getOrder() > other.getOrder()) {
            return 1;
        } else if (this.getOrder() < other.getOrder()) {
            return -1;
        }
        return 0;
    }
}
