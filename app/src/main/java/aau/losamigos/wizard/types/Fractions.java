package aau.losamigos.wizard.types;

import java.util.Random;

/**
 * Created by flo on 10.04.2018.
 */

public enum Fractions {
    green,
    blue,
    yellow,
    red;

    public static String getRandomFraction() {
        int pickFraction = new Random().nextInt(Fractions.values().length);
        Random randNumber = new Random();
        return Fractions.values()[pickFraction].toString()+String.valueOf(randNumber.nextInt(99-10)+10);
    }
}
