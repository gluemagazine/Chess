package dataaccess;

public class InvalidGameNameException extends RuntimeException {
    public InvalidGameNameException(String message) {
        super(message);
    }
}
