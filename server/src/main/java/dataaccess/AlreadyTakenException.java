package dataaccess;

/**
 * indicates that the username already exists
 */
public class AlreadyTakenException extends Exception {
    public AlreadyTakenException(String message) {
        super(message);
    }
    public AlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
