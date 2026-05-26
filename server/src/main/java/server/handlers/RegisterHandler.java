package server.handlers;

import com.google.gson.Gson;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadCredentialsException;
import dataaccess.Exceptions.DataSQLException;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.*;

public class RegisterHandler extends BasicHandler{
    public RegisterHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("I registered a user!!");
        Gson gson = new Gson();

        RegisterRequest request = gson.fromJson(context.body(),RegisterRequest.class);

        try {
            RegisterResult result = users.register(request);
            context.json(gson.toJson(result));
            context.status(200);
        }
        catch (AlreadyTakenException ex) {
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(403);
        }
        catch (BadCredentialsException ex ){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(400);
        } catch (DataSQLException ex){
            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(500);
        }
    }
}
