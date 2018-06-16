package aau.losamigos.wizard.base;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;

import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.rules.JesterRule;
import aau.losamigos.wizard.rules.PointsRule;
import aau.losamigos.wizard.rules.TrumpCardRule;
import aau.losamigos.wizard.rules.WizardRule;
import java.util.HashMap;
import aau.losamigos.wizard.network.DataCallback;


/**
 * Created by gunmic on 13.04.18.
 */

public class GameConfig {

    private static GameConfig singleton = null;
    private boolean host = false;
    private int minPlayer, maxPlayer;
    private boolean keyEnabled;
    private boolean cheatEnabled;
    private Player[] players;
    HashMap<Player, SalutDevice> playerDeviceMap = new HashMap<>();
    private static Salut salut;
    private static DataCallback callback;
    private static GamePlay currentGamePlay;

    private GameConfig(){
        //Singleton pattern, to defeat instantiation
    }
    //Usage for Host
    public static GameConfig getInstance(int minPlayer, int maxPlayer, boolean keyEnabled, boolean cheatEnabled){
        if(singleton == null){
            singleton = new GameConfig(minPlayer, maxPlayer, keyEnabled, cheatEnabled);
        }
        return singleton;
    }
    //Usage for Client
    public static GameConfig getInstance(){
        if(singleton == null){
            singleton = new GameConfig();
            return singleton;
        }
        else
            return singleton;
    }
    private GameConfig(int minPlayer, int maxPlayer, boolean keyEnabled, boolean cheatEnabled){
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.keyEnabled = keyEnabled;
        this.cheatEnabled = cheatEnabled;

        List<AbstractRule> rules = new ArrayList<AbstractRule>();
        rules.add(new JesterRule());
        rules.add(new PointsRule());
        rules.add(new TrumpCardRule());
        rules.add(new WizardRule());
        RuleEngine.getInstance().initializeRules(rules);
    }

    /**
     * @param playerList: Array List of Players from Queue
     * @return true (if Players were added); false (if Players were not added)
     */
    public boolean setPlayers(ArrayList<SalutDevice> playerList){
        if(players != null){
            throw new IllegalStateException("GameConfig: Players may only be instantiated once.");
        }
        else if(playerList.size() == 0){
            Log.d("Players not added","Players not added because of empty playerList.");
            return false;
        }
        else if(playerList.size() < minPlayer || playerList.size() > maxPlayer){
            Log.d("Players not added","Players not added cause of min/max Players criterion.");
            return false;
        }
        players = new Player[playerList.size()];
        int i = 0;
        for (SalutDevice device : playerList) {
            Player player =new Player(device.deviceName);
            player.setSalutDevice(device);
            players[i] = player;
            playerDeviceMap.put(players[i], device);
            i++;
        }
        return true;
    }

    public Player[] getPlayers(){
        return players;
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

    public boolean isHost() {
        return host;
    }


    public void setIsHost(boolean host) {
        this.host = host;
    }

    public Salut getSalut() {
        return salut;
    }

    public DataCallback getCallBack() {
        return callback;
    }

    public void setSalut(Salut salut, DataCallback callback) {
        GameConfig.callback = callback;
        GameConfig.salut = salut;
    }

    public void reset(){
        singleton = null;
    }

    public HashMap<Player, SalutDevice> getPlayerDeviceMap(){
        return playerDeviceMap;
    }

    public GamePlay getCurrentGamePlay() {
        return currentGamePlay;
    }

    public void setCurrentGamePlay(GamePlay currentGamePlay) {
        GameConfig.currentGamePlay = currentGamePlay;
    }
}
