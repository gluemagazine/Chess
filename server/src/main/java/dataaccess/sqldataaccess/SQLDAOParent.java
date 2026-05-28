package dataaccess.sqldataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataSQLException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDAOParent {
    protected String createStatement;

    protected Connection getConnection() throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);
            return connection;

        } catch (SQLException e){
            throw new DataSQLException(e.getMessage(),e);
        }
    }

    protected void closeConnection(Connection connection)throws DataAccessException{
        try {
            connection.commit();
        } catch (SQLException e){
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DataSQLException(ex.getMessage(),ex);
            }
            throw new DataSQLException(e.getMessage(),e);
        }
    }

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
