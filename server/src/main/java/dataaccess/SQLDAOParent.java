package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataSQLException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDAOParent {
    protected String createStatement;

    protected void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataSQLException(String.format("Unable to configure database: %s", ex.getMessage()),ex);
        }
    }
}
