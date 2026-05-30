package dataaccess.sqldataaccess;

import dataaccess.daointerfaces.AuthDAO;
import exceptions.DataAccessException;
import exceptions.DataSQLException;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class MySQLAuthDAO extends SQLDAOParent implements AuthDAO {

    public MySQLAuthDAO() throws DataAccessException {
        try {
            createStatement = """
            CREATE TABLE if not exists `auth` (
              `authToken` varchar(200) NOT NULL,
              `username` varchar(100) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """;
            configureDatabase();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage(),e);
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void clear() throws DataAccessException {
        Connection connection = getConnection();
        String sql = "delete from auth";

        try {
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataSQLException(e.getMessage(),e);
        }

        closeConnection(connection);
    }

    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();

            HashMap<String,String> tokens = new HashMap<>();

            String sql = "select authToken, username from auth";

            try(PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

                while(rs.next()) {
                    tokens.put(rs.getString("authToken"),rs.getString("username"));
                }
            }

            if(tokens.containsKey(authToken)){
                return new AuthData(authToken,tokens.get(authToken));
            }
            return null;

        } catch (SQLException e){

            throw new DataSQLException(e.getMessage(),e);
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

            closeConnection(connection);
        } catch (SQLException e){
            throw new DataSQLException(e.getMessage(),e);
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

            closeConnection(connection);
        } catch (SQLException e){
            throw new DataSQLException(e.getMessage(),e);
        }
    }
}
