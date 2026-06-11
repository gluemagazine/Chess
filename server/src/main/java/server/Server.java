package server;

import dataaccess.*;
import io.javalin.*;
import server.handlers.*;
import server.websocket.WebSocketHandler;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        DataAccessBundle bundle = new DataAccessBundle(false);

        UserService users = new UserService(bundle.userDAO, bundle.authDAO);
        DataService auth = new DataService(bundle.authDAO, bundle.gameDAO, bundle.userDAO);
        GameService games = new GameService(bundle.gameDAO, bundle.authDAO, bundle.userDAO);

        WebSocketHandler webSocketHandler = new WebSocketHandler(bundle);

        // Register your endpoints and exception handlers here.
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", new RegisterHandler(users, auth, games));
        javalin.post("/session", new LoginHandler(users, auth, games));
        javalin.delete("/session", new LogOutHandler(users, auth, games));
        javalin.get("/game", new ListGamesHandler(users, auth, games));
        javalin.post("/game", new CreateGameHandler(users, auth, games));
        javalin.put("/game", new JoinGameHandler(users, auth, games));
        javalin.delete("/db", new DeleteDBHandler(users, auth, games));
        javalin.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
