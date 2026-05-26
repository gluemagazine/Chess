package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataSQLException;
import dataaccess.exceptions.InvalidAuthException;
import io.javalin.http.Context;
import model.LogoutRequest;
import org.jetbrains.annotations.NotNull;
import service.DataService;
import service.GameService;
import service.UserService;

public class LogOutHandler extends BasicHandler{
    public LogOutHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("I logged out!!!");

        Gson gson = new Gson();

        LogoutRequest request = new LogoutRequest(context.header("authorization"));
        try {
            users.logout(request);
            context.status(200);
        }
        catch(InvalidAuthException ex){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(401);
        } catch (DataSQLException ex){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(500);
        }
    }
}
