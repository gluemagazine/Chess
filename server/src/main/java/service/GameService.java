package service;

import chess.ChessGame;
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
        AuthData result = auth.getAuthFromToken(request.authToken());
//        System.out.println("Request: " + request);
//        System.out.println("Result: " + result);
        if(result == null){
            throw new InvalidAuthException("Error: unauthorized");
        }
        if(request.gameID() == null){
            throw new BadDataException("Error: bad request");
        }
        GameData response = games.getGame(request.gameID());
//        System.out.println("Response: " + response);
//        System.out.println(response.whiteUsername());
//        System.out.println(response.blackUsername());


        if (response == null){
            throw new BadDataException("Error: bad request");
        }
        if(request.playerColor() == null){
            throw new BadDataException("Error: bad request");
        }
        String desiredUsername = (request.playerColor() == ChessGame.TeamColor.WHITE) ? response.whiteUsername() : response.blackUsername();
//        System.out.println("Desired username: " + desiredUsername);
        if(desiredUsername != null){
            throw new AlreadyTakenException("Error: already taken");
        }
        String player = result.username();
//        System.out.println("the requesting user: " + player);
        GameData newData = (request.playerColor() == ChessGame.TeamColor.WHITE) ? response.changeWhite(player) : response.changeBlack(player);
//        System.out.println("GameData after adding the player: " + newData);
        games.updateGame(request.gameID(),newData);

    }

    public ListGamesResult listGames(ListGamesRequest request) throws Exception{
        AuthData result = auth.getAuthFromToken(request.authToken());
        if(result == null){
            throw new InvalidAuthException("Error: unauthorized");
        }

        return new ListGamesResult(games.listGames());
    }

    public void clear(){
        games.clear();
    }
}
