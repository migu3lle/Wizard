package aau.losamigos.wizard.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.Card;
import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.exceptions.NoWinnerDeterminedException;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

public class RuleEngineTest {

    private class TheDwarfWins extends AbstractRule {

        @Override
        public Player CheckMove(List<MoveTuple> moves) {
            Player winner = null;
            for(MoveTuple move : moves) {
                if(move.getCard().getFraction() == Fractions.Dwarf) {
                    winner = move.getPlayer();
                }
            }
            return winner;
        }

        @Override
        public int getWeight() {
            return 10;
        }

    }

    private class TheHumanWins extends AbstractRule {

        @Override
        public Player CheckMove(List<MoveTuple> moves) {
            Player winner = null;
            for(MoveTuple move : moves) {
                if(move.getCard().getFraction() == Fractions.Human) {
                    winner = move.getPlayer();
                }
            }
            return winner;
        }

        @Override
        public int getWeight() {
            return 1;
        }
    }

    @Before
    public void setUp() {
        //get ruleEngine and initialize some rule
        RuleEngine ruleEngine = RuleEngine.getInstance();
        List<AbstractRule> rules = new ArrayList<>();
        rules.add(new TheDwarfWins());
        rules.add(new TheHumanWins());
        ruleEngine.initializeRules(rules);
    }

    @Test
    public void TestRuleEngine1() {
        //create a move
        MoveTuple t1 = new MoveTuple(new Player(1, "hugo"), new Card(1, 1,  Fractions.Dwarf), 1);
        MoveTuple t2 = new MoveTuple(new Player(2, "bert"), new Card(2, 10,  Fractions.Human), 2);
        List<MoveTuple> move = new ArrayList<>();
        move.add(t1);
        move.add(t2);

        RuleEngine ruleEngine = RuleEngine.getInstance();
        Player winner = null;
        try {
            winner = ruleEngine.processRound(move);
        } catch (NoWinnerDeterminedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("the dwarf should have won", t1.getPlayer(), winner);
        Assert.assertEquals("the winner should have 10 points", 1, winner.getActualStiches());
    }

    @Test
    public void TestRuleEngine2() {
        //create a move
        MoveTuple t1 = new MoveTuple(new Player(1, "hugo"), new Card(1, 1,  Fractions.Elb), 1);
        MoveTuple t2 = new MoveTuple(new Player(2, "bert"), new Card(2, 10,  Fractions.Human), 2);
        List<MoveTuple> move = new ArrayList<>();
        move.add(t1);
        move.add(t2);

        RuleEngine ruleEngine = RuleEngine.getInstance();
        Player winner = null;
        try {
            winner = ruleEngine.processRound(move);
        } catch (NoWinnerDeterminedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("the human should have won", t2.getPlayer(), winner);
        Assert.assertEquals("the winner should have 10 points", 1, winner.getActualStiches());
    }

    @Test(expected = NoWinnerDeterminedException.class)
    public void TestNoWinner() throws NoWinnerDeterminedException {
        MoveTuple t1 = new MoveTuple(new Player(1, "hugo"), new Card(1, 1,  Fractions.Elb), 1);
        MoveTuple t2 = new MoveTuple(new Player(2, "bert"), new Card(2, 10,  Fractions.Giant), 2);
        List<MoveTuple> move = new ArrayList<>();
        move.add(t1);
        move.add(t2);

        RuleEngine ruleEngine = RuleEngine.getInstance();
        ruleEngine.processRound(move);
    }
}
