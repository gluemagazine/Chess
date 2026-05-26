package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    private HashMap<Integer,GameData> data;
    private int nextID;

    public MemoryGameDAO(){
        nextID = 1;
        data = new HashMap<>();
    }

    private int getNextID(){
        return nextID++;
    }

    @Override
    public void updateGame(String gameID, GameData gameData){
        this.data.put(Integer.valueOf(gameID),gameData);
    }

    @Override
    public String createGame(String gameName){
        int gameID = getNextID();
        data.remove(gameID);
        data.put(gameID,new GameData(gameID,null,null,gameName,new ChessGame()));
        return String.valueOf(gameID);
    }

    @Override
    public ArrayList<GameData> listGames (){
        return new ArrayList<>(data.values());
    }

    @Override
    public GameData getGame(String gameID){
        return data.get(Integer.valueOf(gameID));
    }

    @Override
    public void clear(){
        data.clear();
    }
}
