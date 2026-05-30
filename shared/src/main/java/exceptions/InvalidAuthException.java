package exceptions;


public class InvalidAuthException extends DataAccessException {
    public InvalidAuthException(String message) {
        super(message);
    }
}
