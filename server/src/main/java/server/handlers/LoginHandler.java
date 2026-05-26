package server.handlers;

import com.google.gson.Gson;
import dataaccess.Exceptions.BadCredentialsException;
import dataaccess.Exceptions.DataAccessException;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.*;

public class LoginHandler extends BasicHandler{
    public LoginHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("This is a login handler");
        Gson gson = new Gson();

        LoginRequest request = gson.fromJson(context.body(),LoginRequest.class);

        try{
            LoginResult result = users.login(request);
            context.json(gson.toJson(result));
            context.status(200);
        }
        catch (BadCredentialsException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(400);
        }
        catch (DataAccessException ex){
            context.json(gson.toJson(new ErrorWraper(ex.getMessage())));
            context.status(401);
        }

    }
}
