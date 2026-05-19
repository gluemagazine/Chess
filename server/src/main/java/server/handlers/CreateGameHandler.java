package server.handlers;

import com.google.gson.Gson;
import dataaccess.InvalidAuthException;
import dataaccess.InvalidGameNameException;
import io.javalin.http.Context;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.ListGamesRequest;
import org.jetbrains.annotations.NotNull;
import service.DataService;
import service.GameService;
import service.UserService;

public class CreateGameHandler extends BasicHandler{
    public CreateGameHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("This is a create game handler!!");
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(context.body(),CreateGameRequest.class);
        request = new CreateGameRequest(context.header("authorization"), request.gameName());

        try {
            CreateGameResult response = games.createGame(request);
            context.json(gson.toJson(response));
            context.status(200);
        } catch(InvalidAuthException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(401);
        } catch (InvalidGameNameException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(400);
        }
    }
}
