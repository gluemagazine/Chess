package dataaccess.daointerfaces;

import dataaccess.exceptions.DataAccessException;
import model.*;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    void updateGame(String gameID, GameData data) throws DataAccessException;
    String createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> listGames () throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
}
