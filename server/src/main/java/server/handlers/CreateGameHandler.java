package server.handlers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class CreateGameHandler extends BasicHandler{
    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("This is a create game handler!!");
    }
}
