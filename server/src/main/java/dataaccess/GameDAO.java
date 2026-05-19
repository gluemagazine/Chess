package dataaccess;

import model.*;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    void updateGame(String gameID, GameData data);
    String createGame(String gameName);
    ArrayList<GameData> listGames ();
    GameData getGame(String gameID);
}
