package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    private final HashMap<Integer,GameData> data;
    private int nextID;

    public MemoryGameDAO(){
        nextID = 1;
        data = new HashMap<>();
    }

    private int getNextID(){
        return nextID++;
    }

    public void updateGame(String gameID, GameData gameData){
        this.data.put(Integer.valueOf(gameID),gameData);
    }

    public String createGame(String gameName){
        int ID = getNextID();
        data.put(ID,new GameData(ID,"","",gameName,new ChessGame()));
        return String.valueOf(ID);
    }

    public ArrayList<GameData> listGames (){
        return new ArrayList<>(data.values());
    }

    public GameData getGame(String gameID){
        return data.get(Integer.valueOf(gameID));
    }

    public void clear(){
        data.clear();
    }
}
