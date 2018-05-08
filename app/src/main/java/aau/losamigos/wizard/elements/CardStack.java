package aau.losamigos.wizard.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aau.losamigos.wizard.R;
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

        this.cards.add(new FractionCard(11, 1, Fractions.blue, R.drawable.blue1));
        this.cards.add(new FractionCard(12, 2, Fractions.blue, R.drawable.blue2));
        this.cards.add(new FractionCard(13, 3, Fractions.blue, R.drawable.blue3));
        this.cards.add(new FractionCard(14, 4, Fractions.blue, R.drawable.blue4));
        this.cards.add(new FractionCard(15, 5, Fractions.blue, R.drawable.blue5));
        this.cards.add(new FractionCard(16, 6, Fractions.blue, R.drawable.blue6));
        this.cards.add(new FractionCard(17, 7, Fractions.blue, R.drawable.blue7));
        this.cards.add(new FractionCard(18, 8, Fractions.blue, R.drawable.blue8));
        this.cards.add(new FractionCard(19, 9, Fractions.blue, R.drawable.blue9));
        this.cards.add(new FractionCard(110, 10, Fractions.blue, R.drawable.blue10));
        this.cards.add(new FractionCard(111, 11, Fractions.blue, R.drawable.blue11));
        this.cards.add(new FractionCard(112, 12, Fractions.blue, R.drawable.blue12));
        this.cards.add(new FractionCard(113, 13, Fractions.blue, R.drawable.blue13));


        this.cards.add(new FractionCard(21, 1, Fractions.yellow, R.drawable.yellow1));
        this.cards.add(new FractionCard(22, 2, Fractions.yellow, R.drawable.yellow2));
        this.cards.add(new FractionCard(23, 3, Fractions.yellow, R.drawable.yellow3));
        this.cards.add(new FractionCard(24, 4, Fractions.yellow, R.drawable.yellow4));
        this.cards.add(new FractionCard(25, 5, Fractions.yellow, R.drawable.yellow5));
        this.cards.add(new FractionCard(26, 6, Fractions.yellow, R.drawable.yellow6));
        this.cards.add(new FractionCard(27, 7, Fractions.yellow, R.drawable.yellow7));
        this.cards.add(new FractionCard(28, 8, Fractions.yellow, R.drawable.yellow8));
        this.cards.add(new FractionCard(29, 9, Fractions.yellow, R.drawable.yellow9));
        this.cards.add(new FractionCard(210, 10, Fractions.yellow, R.drawable.yellow10));
        this.cards.add(new FractionCard(211, 11, Fractions.yellow, R.drawable.yellow11));
        this.cards.add(new FractionCard(212, 12, Fractions.yellow, R.drawable.yellow12));
        this.cards.add(new FractionCard(213, 13, Fractions.yellow, R.drawable.yellow13));

        this.cards.add(new FractionCard(31, 1, Fractions.green, R.drawable.green1));
        this.cards.add(new FractionCard(32, 2, Fractions.green, R.drawable.green2));
        this.cards.add(new FractionCard(33, 3, Fractions.green, R.drawable.green3));
        this.cards.add(new FractionCard(34, 4, Fractions.green, R.drawable.green4));
        this.cards.add(new FractionCard(35, 5, Fractions.green, R.drawable.green5));
        this.cards.add(new FractionCard(36, 6, Fractions.green, R.drawable.green6));
        this.cards.add(new FractionCard(37, 7, Fractions.green, R.drawable.green7));
        this.cards.add(new FractionCard(38, 8, Fractions.green, R.drawable.green8));
        this.cards.add(new FractionCard(39, 9, Fractions.green, R.drawable.green9));
        this.cards.add(new FractionCard(310, 10, Fractions.green, R.drawable.green10));
        this.cards.add(new FractionCard(311, 11, Fractions.green, R.drawable.green11));
        this.cards.add(new FractionCard(312, 12, Fractions.green, R.drawable.green12));
        this.cards.add(new FractionCard(313, 13, Fractions.green, R.drawable.green13));

        this.cards.add(new FractionCard(41, 1, Fractions.red, R.drawable.red1));
        this.cards.add(new FractionCard(42, 2, Fractions.red, R.drawable.red2));
        this.cards.add(new FractionCard(43, 3, Fractions.red, R.drawable.red3));
        this.cards.add(new FractionCard(44, 4, Fractions.red, R.drawable.red4));
        this.cards.add(new FractionCard(45, 5, Fractions.red, R.drawable.red5));
        this.cards.add(new FractionCard(46, 6, Fractions.red, R.drawable.red6));
        this.cards.add(new FractionCard(47, 7, Fractions.red, R.drawable.red7));
        this.cards.add(new FractionCard(48, 8, Fractions.red, R.drawable.red8));
        this.cards.add(new FractionCard(49, 9, Fractions.red, R.drawable.red9));
        this.cards.add(new FractionCard(410, 10, Fractions.red, R.drawable.red10));
        this.cards.add(new FractionCard(411, 11, Fractions.red, R.drawable.red11));
        this.cards.add(new FractionCard(412, 12, Fractions.red, R.drawable.red12));
        this.cards.add(new FractionCard(413, 13, Fractions.red, R.drawable.red13));

        this.cards.add(new WizardCard(1, R.drawable.zauberer1));
        this.cards.add(new WizardCard(2, R.drawable.zauberer2));
        this.cards.add(new WizardCard(3, R.drawable.zauberer3));
        this.cards.add(new WizardCard(4, R.drawable.zauberer4));
        this.cards.add(new JesterCard(5, R.drawable.narren1));
        this.cards.add(new JesterCard(6, R.drawable.narren1));
        this.cards.add(new JesterCard(7, R.drawable.narren1));
        this.cards.add(new JesterCard(8, R.drawable.narren1));


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
            card = this.avaliableCards.get(this.rand.nextInt(this.avaliableCards.size()));
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

    public AbstractCard getCardById(int id) {
        for(AbstractCard card: cards) {
            if(card.getId() == id)
                return card;
        }
        return null;
    }
}
