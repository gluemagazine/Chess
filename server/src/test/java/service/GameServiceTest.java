package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.InvalidAuthException;
import dataaccess.Exceptions.InvalidGameNameException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameServiceTest {

    private GameService game;
    private UserService user;
    private String goodToken;


    @BeforeEach
    void createServices(){
        DataAccessBundle bundle = new DataAccessBundle(false);
        UserDAO users = bundle.userDAO;
        AuthDAO auth = bundle.authDAO;
        GameDAO games = bundle.gameDAO;
        game = new GameService(games,auth,users);
        user = new UserService(users,auth);
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            goodToken = result.authToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameSuccessful() {
        try {
            game.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ListGamesResult listResult;
        try {
            listResult = game.listGames(new ListGamesRequest(goodToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("Game1",listResult.games().getFirst().gameName());
        Assertions.assertEquals(1,listResult.games().getFirst().gameID());
        Assertions.assertEquals(new ChessGame(),listResult.games().getFirst().game());
        Assertions.assertNull(listResult.games().getFirst().blackUsername());
        Assertions.assertNull(listResult.games().getFirst().whiteUsername());

    }

    @Test
    void createGameBadAuth(){
        Assertions.assertThrows(InvalidAuthException.class,() ->game.createGame(new CreateGameRequest("abc","Game1")));
    }

    @Test
    void createGameBadName(){
        Assertions.assertThrows(InvalidGameNameException.class,() ->game.createGame(new CreateGameRequest(goodToken,null)));
    }

    @Test
    void joinGameSuccessful() {
        try {
            game.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"1"));
            game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.BLACK,"1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ListGamesResult listResult;
        try {
            listResult = game.listGames(new ListGamesRequest(goodToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("ExistingUser",listResult.games().getFirst().whiteUsername());
        Assertions.assertEquals("ExistingUser",listResult.games().getFirst().blackUsername());
    }

    @Test
    void joinGameBadAuth(){
        try {
            game.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(InvalidAuthException.class,() ->game.joinGame(new JoinGameRequest("abc", ChessGame.TeamColor.WHITE,"1")));
    }

    @Test
    void joinGameAlreadyTaken(){
        try {
            game.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"1"));
            game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.BLACK,"1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(AlreadyTakenException.class,() ->game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"1")));
    }

    @Test
    void listGames() {
        try {
            game.createGame(new CreateGameRequest(goodToken,"Game1"));
            game.createGame(new CreateGameRequest(goodToken,"Game2"));
            game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"2"));
            game.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.BLACK,"2"));
            game.createGame(new CreateGameRequest(goodToken,"Game3"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ListGamesResult listResult;
        try {
            listResult = game.listGames(new ListGamesRequest(goodToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("Game1",listResult.games().getFirst().gameName());
        Assertions.assertEquals(1,listResult.games().getFirst().gameID());
        Assertions.assertEquals(new ChessGame(),listResult.games().getFirst().game());
        Assertions.assertNull(listResult.games().getFirst().blackUsername());
        Assertions.assertNull(listResult.games().getFirst().whiteUsername());

        Assertions.assertEquals("Game2",listResult.games().get(1).gameName());
        Assertions.assertEquals(2,listResult.games().get(1).gameID());
        Assertions.assertEquals(new ChessGame(),listResult.games().get(1).game());
        Assertions.assertEquals("ExistingUser",listResult.games().get(1).whiteUsername());
        Assertions.assertEquals("ExistingUser",listResult.games().get(1).blackUsername());

        Assertions.assertEquals("Game3",listResult.games().get(2).gameName());
        Assertions.assertEquals(3,listResult.games().get(2).gameID());
        Assertions.assertEquals(new ChessGame(),listResult.games().get(2).game());
        Assertions.assertNull(listResult.games().get(2).blackUsername());
        Assertions.assertNull(listResult.games().get(2).whiteUsername());
    }

    @Test
    void clear() {
        listGames();

        game.clear();

        ListGamesResult listResult;
        try {
            listResult = game.listGames(new ListGamesRequest(goodToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertTrue(listResult.games().isEmpty());

    }
}