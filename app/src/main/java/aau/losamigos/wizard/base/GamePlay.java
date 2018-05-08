package aau.losamigos.wizard.base;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by Andreas.Mairitsch on 02.05.2018.
 */

public class GamePlay {
    private List<Player> players;
    private int playerNumber;
    private int maxRounds;

    private Round recentRound;

    public GamePlay(Player[] players) {
        this.players = new ArrayList<Player>();

        for (Player player:players) {
            this.players.add(player);
        }
        this.playerNumber = this.players.size();
        this.maxRounds = 60 / this.playerNumber;
    }
    public void startGame(){
        for (int i = 0; i < maxRounds; i++) {
            Round round = new Round(players,i+1);
        }
    }

    public void startGame(int cardCount) {
        recentRound = new Round(players, cardCount);
    }

    public Round getRecentRound() {
        return this.recentRound;
    }
}
