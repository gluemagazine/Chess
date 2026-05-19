package server;

import dataaccess.*;
import dataaccess.UserDAO;
import io.javalin.*;
import server.handlers.*;
import service.*;

public class Server {

    private final Javalin javalin;

    private final UserService users;
    private final DataService auth;
    private final GameService games;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        users = new UserService(userDAO,authDAO);
        auth = new DataService(authDAO);
        games = new GameService(new MemoryGameDAO(),authDAO,userDAO);

        // Register your endpoints and exception handlers here.
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", new RegisterHandler(users,auth,games));
        javalin.post("/session", new LoginHandler(users,auth,games));
        javalin.delete("/session", new LogOutHandler(users,auth,games));
        javalin.get("/game", new ListGamesHandler(users,auth,games));
        javalin.post("/game", new CreateGameHandler(users,auth,games));
        javalin.put("/game", new JoinGameHandler(users,auth,games));
        javalin.delete("/db", new DeleteDBHandler(users,auth,games));



    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
