package dataaccess.exceptions;

public class InvalidGameNameException extends DataAccessException {
    public InvalidGameNameException(String message) {
        super(message);
    }
}
