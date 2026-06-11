package server.websocket;


import com.google.gson.Gson;
import dataaccess.DataAccessBundle;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.commands.UserMakeMoveCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccessBundle bundle;

    public WebSocketHandler(DataAccessBundle bundle){
        this.bundle = bundle;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                command = new Gson().fromJson(ctx.message(), UserMakeMoveCommand.class);
            }
            switch (command.getCommandType()) {
                case MAKE_MOVE -> makeMove();
                case LEAVE -> leave();
                case RESIGN -> resign();
                case CONNECT -> connect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void makeMove() throws IOException{

    }

    private void leave() throws IOException{

    }

    private void resign() throws IOException{

    }

    private void connect() throws IOException{

    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    public void makeNoise(int gameID, String petName, String sound) throws Exception {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(gameID, null, notification);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}