package aau.losamigos.wizard.base;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by Andreas.Mairitsch on 16.04.2018.
 */

public class Hand {

    private AbstractCard handCards[];
    private Player handOwner;

    public Hand(AbstractCard[] handCards) {
        this.handCards = handCards;
    }
    public Hand(AbstractCard[] handCards, Player handOwner) {
        this.handCards = handCards;
        this.handOwner = handOwner;
    }

    public Player getHandOwner() {
        return handOwner;
    }

    public void setHandOwner(Player handOwner) {
        this.handOwner = handOwner;
    }


    public AbstractCard[] getHandCards() {

        return handCards;
    }

    public AbstractCard[] getAllowedCards(AbstractCard[] table) {

        for (AbstractCard card:handCards) {
            //TODO implementation: is card allowed to play or not?

        }

        return handCards;
    }
}
