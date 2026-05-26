package server.handlers;

import io.javalin.http.Handler;
import service.DataService;
import service.GameService;
import service.UserService;


public abstract class BasicHandler implements Handler {
    protected final UserService users;
    protected final DataService auth;
    protected final GameService games;

    public BasicHandler(UserService users,DataService auth,GameService games){
        this.users = users;
        this.auth = auth;
        this.games = games;
    }

    public record ErrorWrapper(String message){
    }
}
