package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataSQLException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.DataService;
import service.GameService;
import service.UserService;

public class DeleteDBHandler extends BasicHandler{
    public DeleteDBHandler(UserService users, DataService auth, GameService games) {
        super(users, auth, games);
    }

    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        System.out.println("This is a delete db handler");
        Gson gson = new Gson();
        try {
            users.clear();
            auth.clear();
            games.clear();
        } catch(DataSQLException ex){

            context.json(gson.toJson(new ErrorWrapper(ex.getMessage())));
            context.status(500);
        }

    }
}
