package aau.losamigos.wizard.base;

/**
 * Created by gunmic on 13.04.18.
 * Class holds general settings of the game started.
 */

public class GameConfig {
    private String name;
    private int minPlayer, maxPlayer;
    private boolean keyEnabled;
    private boolean cheatEnabled;
    private int playerCount;

    public GameConfig(String name, int minPlayer, int maxPlayer, boolean keyEnabled, boolean cheatEnabled){
        this.name = name;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.keyEnabled = keyEnabled;
        this.cheatEnabled = cheatEnabled;
    }


    public boolean isKeyEnabled() {
        return keyEnabled;
    }

    public boolean isCheatEnabled() {
        return cheatEnabled;
    }

    public void setCheatEnabled(boolean cheatEnabled) {
        this.cheatEnabled = cheatEnabled;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }
}
