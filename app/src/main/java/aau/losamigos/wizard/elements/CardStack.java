package aau.losamigos.wizard.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.cards.JesterCard;
import aau.losamigos.wizard.elements.cards.WizardCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 18.04.2018.
 */

public class CardStack {

    private List<AbstractCard> cards;

    private List<AbstractCard> avaliableCards;

    private Random rand;

    public CardStack() {
        rand = new Random();
        this.initializeCards();
        this.avaliableCards = new ArrayList<>(this.cards);
    }



    private void initializeCards() {
        this.cards = new ArrayList<>();
        Fractions[] fractions =  Fractions.values();
        int cardId = 1;
        for(int i = 0; i < fractions.length; i++) {
            for(int value = 1; value <= 13; value++) {
                this.cards.add(new FractionCard(++cardId, value, fractions[i]));
            }
            this.cards.add(new WizardCard(++cardId));
            this.cards.add(new JesterCard(++cardId));
        }
    }

    /**
     * method that returns cards from the stack
     * @param count the number of cards you want to receive
     * @return
     */
    public List<AbstractCard> getCards(int count) {
        if(count > this.getAvailableCount()) {
            throw new IndexOutOfBoundsException("the number of requested cards is higher than the number available");
        }
        List<AbstractCard> cardsToReturn = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            int cardIndex = this.rand.nextInt(this.avaliableCards.size());
            cardsToReturn.add(this.avaliableCards.get(cardIndex));
            this.avaliableCards.remove(cardIndex);
        }

        return cardsToReturn;
    }

    /**
     * returns a card that is not trump
     * @return
     */
    public AbstractCard getTrump() {
        AbstractCard card;
        do {
            card = this.cards.get(this.rand.nextInt(this.cards.size()));
        } while (card instanceof WizardCard);
        return card;
    }

    /**
     * resets the stack; all cards are back in the deck
     */
    public void reset() {
        this.avaliableCards = new ArrayList<>(this.cards);
    }

    /**
     * method to get the number of created cards
     * mainly for testing purposes
     * @return
     */
    public int getCount() {
        return cards.size();
    }

    /**
     * method to get the number of available cards
     * mainly for testing purposes
     * @return
     */
    public int getAvailableCount() {
        return avaliableCards.size();
    }

}
