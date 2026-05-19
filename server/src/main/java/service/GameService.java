package service;

import dataaccess.GameDAO;

public class GameService {
    private final GameDAO games;
    public GameService(GameDAO games){
        this.games = games;
    }
    public void clear(){
        games.clear();
    }
}
