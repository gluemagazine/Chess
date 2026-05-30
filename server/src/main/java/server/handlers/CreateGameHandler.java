package server.handlers;

import com.google.gson.Gson;
import exceptions.*;
import io.javalin.http.Context;
import model.CreateGameRequest;
import model.CreateGameResult;
import org.jetbrains.annotations.NotNull;
import service.DataService;
import service.GameService;
import service.UserService;

public class CreateGameHandler extends BasicHandler{
    public CreateGameHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        System.out.println("This is a create game handler!!");
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(context.body(),CreateGameRequest.class);
        request = new CreateGameRequest(context.header("authorization"), request.gameName());

        try {
            CreateGameResult response = games.createGame(request);
            context.json(gson.toJson(response));
            context.status(200);
        } catch(InvalidAuthException ex){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(401);
        } catch (InvalidGameNameException ex){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(400);
        } catch (DataSQLException ex){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(500);
        }
    }
}
