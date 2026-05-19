package dataaccess;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
    public BadCredentialsException(String message, Throwable ex) {
        super(message, ex);
    }
}
