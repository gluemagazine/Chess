package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import dataaccess.Exceptions.ResponseException;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAO implements AuthDAO{

    public MySQLAuthDAO() {
        try {
            configureDatabase();
        } catch (Exception _){

        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void clear() {
//        Connection connection = null;
//        try {
//            connection = DatabaseManager.getConnection();
//        } catch (SQLException e){
//            if(connection != null && !connection.isClosed()){
//                connection.rollback();
//            }
//
//        }
    }

    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        /*
        Insert code to get this from the database
         */ 
        return null;
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String token = generateToken();

        /*
        Insert code to put this into the database
         */

        return token;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {
        /*
        Insert code to remove this from the database
         */
    }

    private final String[] createStatements = {
            """
            CREATE TABLE `auth` (
              `authToken` varchar(200) NOT NULL,
              `username` varchar(100) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
