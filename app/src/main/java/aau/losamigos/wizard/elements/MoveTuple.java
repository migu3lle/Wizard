package aau.losamigos.wizard.elements;

/**
 * Created by flo on 10.04.2018.
 */

//maybe there is a better name for this class
public class MoveTuple {

    private Player player;

    private Card card;

    private int order;

    public MoveTuple(Player player, Card card, int order) {
        this.player = player;
        this.card = card;
        this.order = order;
    }

    public Player getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }

    public int getOrder() {
        return order;
    }
}
