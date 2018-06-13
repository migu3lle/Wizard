package aau.losamigos.wizard.base;

import android.webkit.JavascriptInterface;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.PlayerRoundState;
import aau.losamigos.wizard.rules.Actions;
import aau.losamigos.wizard.rules.Client2HostAction;

/**
 * Created by gunmic on 30.04.18.
 */

@JsonObject
public class Message{

    /*
     * Annotate a field that you want sent with the @JsonField marker.
     */
    @JsonField
    public String description;

    @JsonField
    public int[] cards;

    @JsonField
    public int trumpCard;

    @JsonField
    public int playedCard;

    @JsonField
    public int[] playerPoints;

    @JsonField
    public int forbiddenTricks;

    @JsonField
    public int predictedTricks;

    @JsonField
    public int playerCount;

    @JsonField
    public int roundCount;

    @JsonField
    public int action;

    @JsonField
    public int client2HostAction;

    @JsonField
    public String sender;

    @JsonField
    public Map<String, List<PlayerRoundState>> playerStates;

    @JsonField
    public int [] getPlayerHand;

    @JsonField
    public String cheatPlayer;

    @JsonField
    public boolean cheatEnabled;


    @Override
    public String toString() {
        return "Message{" +
                "description='" + description + '\'' +
                ", cards=" + cards +
                ", trump=" + trumpCard +
                ", action=" + action +
                ", client2HostAction=" + client2HostAction +
                '}';
    }
}
