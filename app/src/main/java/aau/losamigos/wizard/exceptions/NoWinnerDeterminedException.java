package aau.losamigos.wizard.exceptions;

/**
 * Created by flo on 10.04.2018.
 */

public class NoWinnerDeterminedException extends Exception {

    public NoWinnerDeterminedException() { super(); }
    public NoWinnerDeterminedException(String message) { super(message); }
    public NoWinnerDeterminedException(String message, Throwable cause) { super(message, cause); }
    public NoWinnerDeterminedException(Throwable cause) { super(cause); }
}
