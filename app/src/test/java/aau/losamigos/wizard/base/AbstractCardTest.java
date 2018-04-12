package aau.losamigos.wizard.base;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.cards.WizardCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 12.04.2018.
 */

public class AbstractCardTest {

    @Before
    public void SetUp() {

    }

    @Test
    public void TestTypeMatch() {
        AbstractCard card = new FractionCard(1, 1, Fractions.Dwarf);
        FractionCard fractionCard = card.getExact(FractionCard.class);
        Assert.assertNotNull("card should have been fractionCard", fractionCard);
    }

    @Test
    public void TestNoTypeMatch() {
        AbstractCard card = new WizardCard(1);
        FractionCard fractionCard = card.getExact(FractionCard.class);
        Assert.assertNull("card should not have been a fractionCard", fractionCard);
    }
}
