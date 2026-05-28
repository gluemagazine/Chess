package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataSQLException;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO extends SQLDAOParent implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        try {
            createStatement = """
            CREATE TABLE if not exists `users` (
              `username` varchar(100) NOT NULL,
              `password` varchar(200) NOT NULL,
              `email` varchar(100) NOT NULL,
              PRIMARY KEY (`username`),
              UNIQUE KEY `userscol_UNIQUE` (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            ;
            configureDatabase();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage(),e);
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "insert into users (username, password, email) values (?, ?, ?)";

            try(PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setString(1, userData.username());
                stmt.setString(2, userData.password());
                stmt.setString(3, userData.email());

                stmt.executeUpdate();
            }

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

    @Override
    public UserData getUser(String username) throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();

            String sql = "select username, password, email from users";

            try(PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

                while(rs.next()) {
                    String thisUsername = rs.getString("username");
                    String thisPassword = rs.getString("password");
                    String thisEmail = rs.getString("email");
                    if(thisUsername.equals(username)){
                        return new UserData(thisUsername,thisPassword,thisEmail);
                    }

                }
            }
            return null;

        } catch (SQLException e){

            throw new DataSQLException(e.getMessage(),e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "delete from users";

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
                throw new DataSQLException(ex.getMessage(),ex);
            }
            throw new DataSQLException(e.getMessage(),e);
        }
    }


}
