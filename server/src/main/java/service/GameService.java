package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class GameService {
    private final GameDAO games;
    private final AuthDAO auth;
    private final UserDAO user;
    public GameService(GameDAO games, AuthDAO auth, UserDAO user){
        this.games = games;
        this.auth = auth;
        this.user = user;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws Exception{
        AuthData result = auth.getAuthFromToken(request.authToken());
        if(result == null){
            throw new InvalidAuthException("Error: unauthorized");
        }
        if(request.gameName() == null){
            throw new InvalidGameNameException("Error: bad request");
        }

        return new CreateGameResult(games.createGame(request.gameName()));
    }

    public void joinGame(JoinGameRequest request) throws Exception{

    }

    public ArrayList<GameData> listGames(ListGamesRequest request) throws Exception{
        AuthData result = auth.getAuthFromToken(request.authToken());
        if(result == null){
            throw new InvalidAuthException("Error: unauthorized");
        }

        return games.listGames();
    }

    public void clear(){
        games.clear();
    }
}
