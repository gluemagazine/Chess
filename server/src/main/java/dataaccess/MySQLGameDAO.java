package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO{
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
}
