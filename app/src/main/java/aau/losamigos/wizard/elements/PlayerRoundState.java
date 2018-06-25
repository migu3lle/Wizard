package aau.losamigos.wizard.elements;

import com.bluelinelabs.logansquare.annotation.JsonField;

import java.io.Serializable;

/**
 * Created by flo on 11.06.2018.
 */

public class PlayerRoundState implements Serializable {


    public int getActualStiches() {
        return actualStiches;
    }

    public void setActualStiches(int actualStiches) {
        this.actualStiches = actualStiches;
    }

    public int getCalledStiches() {
        return calledStiches;
    }

    public void setCalledStiches(int calledStiches) {
        this.calledStiches = calledStiches;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    private int actualStiches;


    private int calledStiches;


    private int points;

    public PlayerRoundState(int actual, int estimated, int points) {
        this.actualStiches = actual;
        this.calledStiches = estimated;
        this.points = points;
    }
}
