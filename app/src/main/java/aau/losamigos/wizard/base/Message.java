package aau.losamigos.wizard.base;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.rules.Actions;

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
    public List<AbstractCard> cards;
    
    @JsonField
    public Actions action;

    /*
     * Note that since this field isn't annotated as a
     * @JsonField, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    public int nonJsonField;


}
