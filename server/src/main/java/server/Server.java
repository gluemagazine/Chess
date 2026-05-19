package server;

import io.javalin.*;
import server.handlers.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", new RegisterHandler());
        javalin.post("/session", new LoginHandler());
        javalin.delete("/session", new LogOutHandler());
        javalin.get("/game", new ListGamesHandler());
        javalin.post("/game", new CreateGameHandler());
        javalin.put("/game", new JoinGameHandler());
        javalin.delete("/db", new DeleteDBHandler());


        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
