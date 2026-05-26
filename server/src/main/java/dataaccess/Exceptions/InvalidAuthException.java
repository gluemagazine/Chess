package dataaccess.Exceptions;

import javax.xml.crypto.Data;

public class InvalidAuthException extends DataAccessException {
    public InvalidAuthException(String message) {
        super(message);
    }
}
