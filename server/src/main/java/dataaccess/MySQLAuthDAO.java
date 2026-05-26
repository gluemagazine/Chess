package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import dataaccess.Exceptions.ResponseException;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
    public void clear() throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "delete from auth";

            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e){
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage(),ex);
            }
            throw new DataAccessException(e.getMessage(),e);
        }
    }

    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        /*
        Insert code to get this from the database
         */
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();

            HashMap<String,String> tokens = new HashMap<>();

            String sql = "select authToken, username from auth";

            try(PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

                while(rs.next()) {
                    tokens.put(rs.getString("authtoken"),rs.getString("username"));
                }
            }

            if(tokens.containsKey(authToken)){
                return new AuthData(authToken,tokens.get(authToken));
            }
            return null;

        } catch (SQLException e){

            throw new DataAccessException(e.getMessage(),e);
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String token = generateToken();

        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "insert into auth (authToken, username) values (?, ?)";

            try(PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setString(1, token);
                stmt.setString(2, username);

                stmt.executeUpdate();

            }

            connection.commit();
        } catch (SQLException e){
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                return null;
            }
            return null;
        }

        return token;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM auth WHERE authToken = ?;";

            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1,data.authToken());
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e){
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage(),ex);
            }
            throw new DataAccessException(e.getMessage(),e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE `auth` (
              `authToken` varchar(200) NOT NULL,
              `username` varchar(100) NOT NULL,
              PRIMARY KEY (`authToken`)
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
