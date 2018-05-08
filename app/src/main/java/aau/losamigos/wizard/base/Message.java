package aau.losamigos.wizard.base;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.List;

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
    public int action;

    @JsonField
    public int client2HostAction;

    @JsonField
    public String sender;

    /*
     * Note that since this field isn't annotated as a
     * @JsonField, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    public int nonJsonField;


    @Override
    public String toString() {
        return "Message{" +
                "description='" + description + '\'' +
                ", cards=" + cards +
                ", action=" + action +
                ", client2HostAction=" + client2HostAction +
                ", sender='" + sender + '\'' +
                ", nonJsonField=" + nonJsonField +
                '}';
    }
}
