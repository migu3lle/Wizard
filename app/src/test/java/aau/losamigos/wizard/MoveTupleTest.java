package aau.losamigos.wizard;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aau.losamigos.wizard.base.AbstractCard;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.JesterCard;
import aau.losamigos.wizard.elements.cards.WizardCard;

/**
 * Created by flo on 25.06.2018.
 */

public class MoveTupleTest {
    Player p1;
    Player p2;
    AbstractCard card;
    MoveTuple mt;

    @Before
    public void setUp() {
        p1 = new Player("sepp");
        p2 = new Player("harald");
        card = new JesterCard(1, 1);
        mt = new MoveTuple(p1, card, 2);
    }

    @After
    public void tearDown() {
        p1 = null;
        p2 = null;
        mt = null;
        card = null;
    }

    @Test
    public void TestGetter() {
        Player p = mt.getPlayer();
        AbstractCard c = mt.getCard();
        int o = mt.getOrder();
        Assert.assertEquals(p1, p);
        Assert.assertEquals(card, c);
        Assert.assertEquals(2, o);
    }

    @Test
    public void TestGetExactCard() {
        JesterCard jc = mt.getExactCard(JesterCard.class);
        Assert.assertNotNull(jc);
        WizardCard wc = mt.getExactCard(WizardCard.class);
        Assert.assertNull(wc);
    }

    @Test
    public void TestCompareOrder() {
        MoveTuple mt1 = new MoveTuple(p2,card, 1);
        MoveTuple mt2 = new MoveTuple(p2, card, 3);

        int compareResult = mt.compareOrder(mt1);
        Assert.assertEquals(1, compareResult);

        compareResult = mt.compareOrder(mt2);
        Assert.assertEquals(-1, compareResult);

        compareResult = mt.compareOrder(mt);
        Assert.assertEquals(0, compareResult);
    }

}
