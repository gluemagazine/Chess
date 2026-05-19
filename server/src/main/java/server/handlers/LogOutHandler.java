package server.handlers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class LogOutHandler extends BasicHandler{
    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("I logged out!!!");
    }
}
