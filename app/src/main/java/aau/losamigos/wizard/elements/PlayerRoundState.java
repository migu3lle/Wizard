package aau.losamigos.wizard.elements;

import java.io.Serializable;

/**
 * Created by flo on 11.06.2018.
 */

public class PlayerRoundState implements Serializable {
    public int actualStiches;
    public int calledStiches;
    public int points;

    public PlayerRoundState(int actual, int estimated, int points) {
        actualStiches = actual;
        calledStiches = estimated;
        points = points;
    }
}
