package server.handlers;

import io.javalin.http.Context;
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
    }
}
