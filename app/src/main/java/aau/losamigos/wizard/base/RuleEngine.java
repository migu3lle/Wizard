package aau.losamigos.wizard.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import aau.losamigos.wizard.elements.MoveTuple;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by flo on 10.04.2018.
 */

public class RuleEngine {

    private static RuleEngine instance = null;

    private List<AbstractRule> rules;

    private RuleEngine() {
        this.rules = new ArrayList<>();
    }

    /**
     *
     * @return the one and only instance of the rule engine since it is a singleton
     */
    public static RuleEngine getInstance() {
        if(RuleEngine.instance == null) {
            RuleEngine.instance = new RuleEngine();
        }
        return RuleEngine.instance;
    }

    /**
     * this method has to be called at some point in order to initialize the rules
     * that should be checked by the engine
     * @param rules the rules that should be checked during the game
     */
    public void initializeRules(List<AbstractRule> rules) {
        //we do not want to get a nullreference exception later on
        if(rules == null) {
            return;
        }
        //sort the rules descending by their weight
        Collections.sort(rules, new Comparator<AbstractRule>() {
            @Override
            public int compare(AbstractRule r1, AbstractRule r2) {
                return r1.compareToReverse(r2);
            }
        });
        this.rules = rules;
    }

    /**
     * method to reset the rule list
     */
    public void resetRules() {
        this.rules = new ArrayList<>();
    }

    /**
     * this methods takes all player + the cards the played + the order
     * and returns the winner of that stich round
     * all cards are compared against each other according to the defined rules
     * this methos also increases the actual amount of stiches of the player
     * @param moves a tuple containing player, played card and order
     * @param trump the trump for this stich round
     * @return
     */
    public Player processRound(final List<MoveTuple> moves, Fractions trump)  {
        if(moves == null || moves.size() == 0) {
            return null;
        }

        if(moves.size() == 1) {
            return moves.get(0).getPlayer();
        }

        //sort cards ascending by the order they have been played
        Collections.sort(moves, new Comparator<MoveTuple>() {
            @Override
            public int compare(MoveTuple moveTuple, MoveTuple t1) {
                return moveTuple.compareOrder(t1);
            }
        });

        Iterator it = moves.iterator();
        MoveTuple currentWinner = (MoveTuple)it.next();
        while(it.hasNext()) {
            MoveTuple nextCard = (MoveTuple)it.next();
            //check all rules for the current winning card and the next card
            currentWinner = this.checkAllRules(currentWinner, nextCard, trump);
        }

        Player winner = currentWinner.getPlayer();
        winner.increaseStiches();
        return winner;
    }

    private MoveTuple checkAllRules(MoveTuple currentWinner, MoveTuple nextCard, Fractions trump) {
        MoveTuple winner = currentWinner;
        for(AbstractRule rule : this.rules) {
           winner = rule.check(currentWinner, nextCard, trump);
           //if the winner changed, we do not need to check the other rules anymore
           if(winner != currentWinner) {
               break;
           }
        }
        return winner;
    }

}
