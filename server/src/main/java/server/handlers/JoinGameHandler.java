package server.handlers;

import com.google.gson.Gson;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadDataException;
import dataaccess.Exceptions.InvalidAuthException;
import io.javalin.http.Context;
import model.JoinGameRequest;
import org.jetbrains.annotations.NotNull;
import service.DataService;
import service.GameService;
import service.UserService;

public class JoinGameHandler extends BasicHandler{
    public JoinGameHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("This is a join game handler");
        Gson gson = new Gson();

        try {
            JoinGameRequest request = gson.fromJson(context.body(),JoinGameRequest.class);
            request = new JoinGameRequest(context.header("authorization"),request.playerColor(),request.gameID());
            games.joinGame(request);
            context.status(200);
        } catch(InvalidAuthException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(401);
        } catch(BadDataException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(400);
        } catch (AlreadyTakenException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(403);
        }
    }
}
