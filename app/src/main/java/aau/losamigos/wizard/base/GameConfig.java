package aau.losamigos.wizard.base;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by gunmic on 13.04.18.
 */

public class GameConfig {

    private static GameConfig singleton = null;

    private String name;
    private int minPlayer, maxPlayer;
    private boolean keyEnabled;
    private boolean cheatEnabled;
    private int playerCount;

    /*
    TODO Class might should store WIFI connections to Peers too...?
    */
    /*
    TODO Class might should store Player information too...?
    */

    private GameConfig(){
        //Singleton pattern, to defeat instantiation
    }
    public static GameConfig getInstance(String name, int minPlayer, int maxPlayer, boolean keyEnabled, boolean cheatEnabled){
        if(singleton == null){
            singleton = new GameConfig(name, minPlayer, maxPlayer, keyEnabled, cheatEnabled);
        }
        return singleton;
    }
    public static GameConfig getInstance(){
        if(singleton == null){
            throw new IllegalArgumentException("GameConfig: GameConfig not instantiated.");
        }
        else
            return singleton;
    }
    private GameConfig(String name, int minPlayer, int maxPlayer, boolean keyEnabled, boolean cheatEnabled){
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
