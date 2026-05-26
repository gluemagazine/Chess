package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import dataaccess.Exceptions.ResponseException;
import model.GameData;

import java.sql.Connection;
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
    public void clear() {

    }

    @Override
    public void updateGame(String gameID, GameData data) {

    }

    @Override
    public String createGame(String gameName) {
        return "";
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public GameData getGame(String gameID) {
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE `games` (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(100) NOT NULL,
              `whiteUsername` varchar(100) DEFAULT NULL,
              `blackUsername` varchar(45) DEFAULT NULL,
              `JSON` json DEFAULT NULL,
              PRIMARY KEY (`gameID`)
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
