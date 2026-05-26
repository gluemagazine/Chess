package server.handlers;

import com.google.gson.Gson;
import dataaccess.Exceptions.InvalidAuthException;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.*;

public class ListGamesHandler extends BasicHandler{
    public ListGamesHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("This is a list games handler");
        Gson gson = new Gson();

        try {
            ListGamesResult response = games.listGames(new ListGamesRequest(context.header("authorization")));
            context.json(gson.toJson(response));
            context.status(200);
        } catch(InvalidAuthException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(401);
        }
    }
}
