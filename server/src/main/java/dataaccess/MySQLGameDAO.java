package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataSQLException;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO() {
        try {
            configureDatabase();
        } catch (Exception e){
        }
    }

    private String toJson(ChessGame game){
        Gson gson = new Gson();
        return gson.toJson(game);
    }

    private ChessGame fromJson(String gameJson){
        Gson gson = new Gson();
        ChessGame game = gson.fromJson(gameJson,ChessGame.class);
        return game;
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

            sql = "ALTER TABLE games AUTO_INCREMENT = 1";
            try(PreparedStatement resetAutoIncrementStatement = connection.prepareStatement(sql)) {
                resetAutoIncrementStatement.executeUpdate();
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
    public void updateGame(String gameID, GameData data) throws DataAccessException{
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "update games SET gameName = ?, whiteUsername = ?, blackUsername = ?, JSON = ?  where gameID = ?";

            try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


                stmt.setString(1, data.gameName());
                stmt.setString(2, data.whiteUsername());
                stmt.setString(3, data.blackUsername());
                stmt.setString(4, toJson(data.game()));
                stmt.setInt(5, data.gameID());

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
    public String createGame(String gameName) throws DataAccessException {
        Connection connection = null;
        int gameID;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);

            String sql = "insert into games (gameName, whiteUsername, blackUsername, JSON) values (?, ?, ?, ?)";

            try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, gameName);
                stmt.setString(2, null);
                stmt.setString(3, null);
                stmt.setString(4, toJson(new ChessGame()));

                if (stmt.executeUpdate() == 1){
                    try (ResultSet resultSet = stmt.getGeneratedKeys()){
                        resultSet.next();
                        gameID = resultSet.getInt(1);
                    }
                }
                else {
                    gameID = -1;
                }
            }

            connection.commit();
            return String.valueOf(gameID);
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
    public ArrayList<GameData> listGames() throws DataAccessException {
        Connection connection = null;
        ArrayList<GameData> games = new ArrayList<>();
        try {
            connection = DatabaseManager.getConnection();

            String sql = "select gameID, gameName , whiteUsername, blackUsername, JSON from games";

            try(PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

                while(rs.next()) {
                    String thisID = rs.getString("gameID");
                    String gameName = rs.getString("gameName");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String JSON = rs.getString("JSON");
                    GameData temp = new GameData(Integer.parseInt(thisID),whiteUsername,blackUsername,gameName,fromJson(JSON));
                    games.add(temp);
                }
            }
            return games;

        } catch (SQLException e){

            throw new DataSQLException(e.getMessage(),e);
        }
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();

            String sql = "select gameID, gameName , whiteUsername, blackUsername, JSON from games";

            try(PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

                while(rs.next()) {
                    String thisID = rs.getString("gameID");
                    if(thisID.equals(gameID)){
                        String gameName = rs.getString("gameName");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String JSON = rs.getString("JSON");
                        GameData data = new GameData(Integer.parseInt(thisID),whiteUsername,blackUsername,gameName,fromJson(JSON));
                        return data;
                    }

                }
            }
            return null;

        } catch (SQLException e){

            throw new DataSQLException(e.getMessage(),e);
        }
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
            throw new DataSQLException(String.format("Unable to configure database: %s", ex.getMessage()),ex);
        }
    }
}
