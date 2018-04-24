package aau.losamigos.wizard.base;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.JesterCard;
import aau.losamigos.wizard.elements.cards.WizardCard;
import aau.losamigos.wizard.rules.JesterRule;
import aau.losamigos.wizard.rules.PointsRule;
import aau.losamigos.wizard.rules.TrumpCardRule;
import aau.losamigos.wizard.rules.WizardRule;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

public class RuleEngineTest {

    private Player p1;
    private Player p2;
    private Player p3;

    private RuleEngine ruleEngine;

    @Before
    public void setUp() {
        //get ruleEngine and initialize the rules
        ruleEngine = RuleEngine.getInstance();
        List<AbstractRule> rules = new ArrayList<>();
        rules.add(new WizardRule());
        rules.add(new JesterRule());
        rules.add(new TrumpCardRule());
        rules.add(new PointsRule());
        ruleEngine.initializeRules(rules);

        //define some players
        p1 =  new Player("luke");
        p2 =  new Player("lea");
        p3 =  new Player("han");
    }

    @After
    public void tearDown() {
        ruleEngine.resetRules();

        p1 = null;
        p2 = null;
        p3 = null;
    }

    @Test
    public void TestMoveNull() {
        Player winner = ruleEngine.processRound(null, null);
        Assert.assertNull("the object should have been null", winner);
    }

    @Test
    public void TestMoveEmpty() {
        Player winner = ruleEngine.processRound(new ArrayList<MoveTuple>(), null);
        Assert.assertNull("the object should have been null", winner);
    }

    @Test
    public void TestWizardRule() {
        List<MoveTuple> move = new ArrayList<>();
        move.add(new MoveTuple(p1, new FractionCard(2, 11,  Fractions.Human), 1));
        move.add(new MoveTuple(p2, new WizardCard(1), 2));
        move.add(new MoveTuple(p3, new FractionCard(3, 10,  Fractions.Human), 3));

        Player winner = ruleEngine.processRound(move, null);

        Assert.assertEquals("player 2 should have won", p2, winner);
    }

    @Test
    public void TestJesterRule() {
        List<MoveTuple> move = new ArrayList<>();
        move.add(new MoveTuple(p1, new JesterCard(2), 1));
        move.add(new MoveTuple(p2, new FractionCard(1, 2, Fractions.Human), 2));
        move.add(new MoveTuple(p3, new JesterCard(3), 3));

        Player winner = ruleEngine.processRound(move, null);

        Assert.assertEquals("player 2 should have won", p2, winner);
    }

    @Test
    public void TestTrumpRule() {
        List<MoveTuple> move = new ArrayList<>();
        move.add(new MoveTuple(p1, new FractionCard(1, 13,  Fractions.Human), 1));
        move.add(new MoveTuple(p2, new FractionCard(2, 12, Fractions.Human), 2));
        move.add(new MoveTuple(p3, new FractionCard(3, 1,  Fractions.Dwarf), 3));

        Player winner = ruleEngine.processRound(move, Fractions.Dwarf);

        Assert.assertEquals("player 3 should have won", p3, winner);
    }

    @Test
    public void TestPointsRule() {
        List<MoveTuple> move = new ArrayList<>();
        move.add(new MoveTuple(p1, new FractionCard(1, 5,  Fractions.Human), 1));
        move.add(new MoveTuple(p2, new FractionCard(2, 12, Fractions.Human), 2));
        move.add(new MoveTuple(p3, new FractionCard(3, 1,  Fractions.Human), 3));

        Player winner = ruleEngine.processRound(move, Fractions.Dwarf);

        Assert.assertEquals("player 2 should have won", p2, winner);
    }

    @Test
    public void TestStichCount() {
        TestWizardRule(); //p2 wins
        TestJesterRule(); //p2 wins
        TestTrumpRule(); //p3 wins
        TestPointsRule(); //p2 wins

        Assert.assertEquals("player 2 should have 3 stiches", 3, p2.getActualStiches());
        Assert.assertEquals("player 3 should have 1 stich", 1, p3.getActualStiches());
        Assert.assertEquals("player 1 should have 0 stiches", 0, p1.getActualStiches());
    }
}
