package client;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import server.DataAccessException;
import server.Server;
import server.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private String goodToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearServices(){
        try {
            facade.clear();
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameSuccessful() {
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            goodToken = result.authToken();
            facade.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ListGamesResult listResult;
        try {
            listResult = facade.listGames(new ListGamesRequest(goodToken));
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
        Assertions.assertThrows(DataAccessException.class,() ->facade.createGame(new CreateGameRequest("abc","Game1")));
    }

    @Test
    void createGameBadName(){
        Assertions.assertThrows(DataAccessException.class,() ->facade.createGame(new CreateGameRequest(goodToken,null)));
    }

    @Test
    void joinGameSuccessful() {
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            goodToken = result.authToken();
            facade.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"1"));
            facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.BLACK,"1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ListGamesResult listResult;
        try {
            listResult = facade.listGames(new ListGamesRequest(goodToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("ExistingUser",listResult.games().getFirst().whiteUsername());
        Assertions.assertEquals("ExistingUser",listResult.games().getFirst().blackUsername());
    }

    @Test
    void joinGameBadAuth(){
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            goodToken = result.authToken();
            facade.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(DataAccessException.class,() ->facade.joinGame(new JoinGameRequest("abc", ChessGame.TeamColor.WHITE,"1")));
    }

    @Test
    void joinGameAlreadyTaken(){
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            goodToken = result.authToken();
            facade.createGame(new CreateGameRequest(goodToken,"Game1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"1"));
            facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.BLACK,"1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class,() ->facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"1")));
    }

    @Test
    void listGames() {
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            goodToken = result.authToken();
            facade.createGame(new CreateGameRequest(goodToken,"Game1"));
            facade.createGame(new CreateGameRequest(goodToken,"Game2"));
            facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.WHITE,"2"));
            facade.joinGame(new JoinGameRequest(goodToken, ChessGame.TeamColor.BLACK,"2"));
            facade.createGame(new CreateGameRequest(goodToken,"Game3"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ListGamesResult listResult;
        try {
            listResult = facade.listGames(new ListGamesRequest(goodToken));
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
    void loginSuccessful() {
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            facade.logout(new LogoutRequest(result.authToken()));
            LoginResult newResult = facade.loginUser(new LoginRequest("ExistingUser","password"));
            Assertions.assertNotNull(newResult.authToken());
            Assertions.assertEquals("ExistingUser",newResult.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void loginNonExistentUser(){
        Assertions.assertThrows(DataAccessException.class, ()-> facade.loginUser(new LoginRequest("ExistingUser","password")));
    }

    @Test
    void loginBadCredentials(){
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            facade.logout(new LogoutRequest(result.authToken()));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()-> facade.loginUser(new LoginRequest("ExistingUser","badPassword")));
    }

    @Test
    void loginBadRequest(){
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            facade.logout(new LogoutRequest(result.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()-> facade.loginUser(new LoginRequest(null,"null")));
    }

    @Test
    void registerSuccessful() {
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            Assertions.assertNotNull(result.authToken());
            Assertions.assertEquals("ExistingUser",result.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerExistingUser(){
        registerSuccessful();

        Assertions.assertThrows(DataAccessException.class, ()->facade.registerUser(new RegisterRequest("ExistingUser","password","example")));
    }

    @Test
    void registerBadRequest(){
        Assertions.assertThrows(DataAccessException.class, ()->facade.registerUser(new RegisterRequest(null,"password","example")));
        Assertions.assertThrows(DataAccessException.class, ()->facade.registerUser(new RegisterRequest("ExistingUser","password",null)));
        Assertions.assertThrows(DataAccessException.class, ()->facade.registerUser(new RegisterRequest("ExistingUser",null,"example")));
    }

    @Test
    void logout() {
        try {
            RegisterResult result = facade.registerUser(new RegisterRequest("ExistingUser","password","example"));
            facade.logout(new LogoutRequest(result.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
