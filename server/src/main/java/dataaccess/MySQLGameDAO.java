package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import dataaccess.Exceptions.ResponseException;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO() {
        try {
            configureDatabase();
        } catch (Exception _){

        }
    }

    @Override
    public void clear() throws DataAccessException{
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "delete from games";

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
    public void updateGame(String gameID, GameData data) throws DataAccessException{

    }

    @Override
    public String createGame(String gameName) throws DataAccessException {
        return "";
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE `games` (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(100) NOT NULL,
              `whiteUsername` varchar(100) DEFAULT NULL,
              `blackUsername` varchar(45) DEFAULT NULL,
              `JSON` json NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()),ex);
        }
    }
}
