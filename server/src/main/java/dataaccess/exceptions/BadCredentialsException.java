package dataaccess.exceptions;

public class BadCredentialsException extends DataAccessException {
    public BadCredentialsException(String message) {
        super(message);
    }
    public BadCredentialsException(String message, Throwable ex) {
        super(message, ex);
    }
}
