package server.handlers;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
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

        }
        catch (AlreadyTakenException ex) {
            context.json(gson.toJson(ex));
            context.status(403);
        }


    }
}
