package aau.losamigos.wizard.base;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.CardStack;
import aau.losamigos.wizard.elements.Player;

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
            recentRound = new Round(this, 5);
            recentRound.startRound();
        }
        else{
            gameFinished();
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
}
