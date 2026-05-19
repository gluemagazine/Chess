package server.handlers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class RegisterHandler extends BasicHandler{
    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("I registered a user!!");
    }
}
