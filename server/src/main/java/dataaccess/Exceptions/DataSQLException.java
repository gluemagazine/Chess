package dataaccess.Exceptions;

public class DataSQLException extends DataAccessException {
    public DataSQLException(String message) {
        super(message);
    }
    public DataSQLException(String message,Exception ex) {
        super(message,ex);
    }
}
