package aau.losamigos.wizard;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.base.AbstractRule;
import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.cards.WizardCard;

/**
 * Created by flo on 19.04.2018.
 */

public class CardStackTest {

    private CardStack _cardStack;

    @Before
    public void SetUp() {
        _cardStack = new CardStack();
    }

    @After
    public void TearDown() {
        _cardStack = null;
    }

    @Test
    public void TestCardCount() {
        int totalCardCount = _cardStack.getCount();
        int availableCardCount = _cardStack.getAvailableCount();

        Assert.assertEquals("counts should be equal since no cards have been removed from the deck",
                totalCardCount, availableCardCount);

        Assert.assertEquals("60 cards should have been created", 60, totalCardCount);
    }

    @Test
    public void TestGetCards() {
        List<AbstractCard> cards = _cardStack.getCards(4);
        Assert.assertEquals("4 cards should have been returned by the cardStack",
                4, cards.size());

        int availableCardCount = _cardStack.getAvailableCount();
        Assert.assertEquals("the number of available cards should have been reduced by 4",
                56, availableCardCount);

        cards = _cardStack.getCards(6);
        Assert.assertEquals("6 cards should have been returned by the cardStack",
                6, cards.size());

        availableCardCount = _cardStack.getAvailableCount();
        Assert.assertEquals("the number of available cards should have been reduced by 6",
                50, availableCardCount);
    }

    @Test
    public void TestResetCardStack() {
        _cardStack.getCards(30);
        int availableCardCount = _cardStack.getAvailableCount();
        Assert.assertEquals("the number of available cards should have been reduced by 30",
                30, availableCardCount);

        _cardStack.reset();
        availableCardCount = _cardStack.getAvailableCount();
        Assert.assertEquals("all 60 cards should be back in the deck",
                60, availableCardCount);
    }

    @Test
    public void TestGetTrump() {
        //run the command 60 times because it increases the probability that a wizard card is
        //pulled from the deck
        for(int i = 0; i < 60; i++) {
            AbstractCard card = _cardStack.getTrump();
            boolean notAWizard = !(card instanceof WizardCard);
            Assert.assertTrue("a wizard should never be returned as a trump", notAWizard);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void TestNumberTooLarge() {
        _cardStack.getCards(61);
    }

}
