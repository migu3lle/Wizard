package aau.losamigos.wizard.base;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.PlayerRoundState;
import aau.losamigos.wizard.elements.PlayerRoundStateParser;

/**
 * Created by Andreas.Mairitsch on 02.05.2018.
 */

public class GamePlay {
    private List<Player> players; //Instanzen der einzelnen Spieler
    private int playerNumber; //Anzahl Spieler
    private int maxRounds; //letzte Runde
    private int countRound; //Enspricht auch der aktuellen Anzahl von Handkarten pro Runde
    private Round recentRound; //Instanz der aktuellen Runde
    private CardStack cardStack; //Verwendeter Kartenstapel

    public int getCountRound() {
        return countRound;
    }

    public CardStack getCardStack() {
        return cardStack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public GamePlay(Player[] players) {
        countRound =1;
        this.players = new ArrayList<Player>();

        for (Player player:players) {
            this.players.add(player);
        }
        this.playerNumber = this.players.size();
        this.maxRounds = 60 / this.playerNumber;
        this.cardStack = new CardStack();
    }

    /*
    Erhöht die Rundenanzahl um 1 und startet eine neue Runde wenn maximale Rundenanzahl noch nicht erreicht,
    sonst wird gameFinished Methode aufgerufen
     */
    public void nextRound(){
        if(countRound<=maxRounds){
            countRound++;
            cardStack.reset(); //Kartenstapel wird neu gemischt
            resetPlayerStiches();
            recentRound = new Round(this, countRound);
            Log.d("WizardApp", "nextRound(): start new round, count " + countRound);
            recentRound.startRound();
        }
        else{
            gameFinished();
        }

    }

    private void resetPlayerStiches() {
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.restStiches();
        }
    }
    /*
    erledigt alle abschlussarbeiten wenn das Spiel vorbei ist
     */
    public void gameFinished(){
            //TODO Dinge welche nach der letzten Runde erledigt werden müssen
    }

    public void startGame(int cardCount) {
        recentRound = new Round(this, cardCount);
    }

    public Round getRecentRound() {
        return this.recentRound;
    }

    public Map<String, List<String>> getPlayerRoundStates() {
        HashMap<String, List<String>> map = new HashMap<>();
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            List<String> ls = new ArrayList<>();
            for(PlayerRoundState state : player.getRoundStates()) {
                ls.add(PlayerRoundStateParser.convert(state));
            }
            map.put(player.getName(), ls);
        }
        return map;
    }
}
