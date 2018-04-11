package aau.losamigos.wizard.base;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.exceptions.NoWinnerDeterminedException;

/**
 * Created by flo on 10.04.2018.
 */

public class RuleEngine {

    private static RuleEngine instance = null;

    private static List<AbstractRule> rules;

    private RuleEngine() {

    }

    public static RuleEngine getInstance() {
        if(RuleEngine.instance == null) {
            RuleEngine.instance = new RuleEngine();
        }
        return RuleEngine.instance;
    }


    public void initializeRules(List<AbstractRule> rules) {
        //order the rules because of their weight
        Collections.sort(rules, new Comparator<AbstractRule>() {
            @Override
            public int compare(AbstractRule r1, AbstractRule r2) {
                return r1.compareToReverse(r2);
            }
        });
        this.rules = rules;
    }

    public Player processRound(List<MoveTuple> moveTuples) throws NoWinnerDeterminedException {
        Player winner = null;
        for(AbstractRule rule : this.rules) {
            //check each rule
            winner = rule.CheckMove(moveTuples);

            //if the rule applies the winner is not null
            if(winner != null) {
                //add the points of the rule to the total points of the winner
                winner.increaseStiches();

                //break here because the rules are ordered by weight from top to bottom
                //if the first rule applies the others do not need to checked anymore
                break;
            }
            //otherwise continue to check rules until one applies
        }
        if(winner == null) {
            throw new NoWinnerDeterminedException("none of the rules could determine a winner! " +
                    "make sure at least one rule is always able to determine a winner");
        }
        return winner;
    }

}
