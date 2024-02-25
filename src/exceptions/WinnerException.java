package exceptions;

public class WinnerException extends Exception {
    public WinnerException(Character c) {
        super(String.valueOf(c));

    }
}
