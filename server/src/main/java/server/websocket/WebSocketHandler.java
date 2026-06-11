package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessBundle;
import exceptions.DataAccessException;
import io.javalin.websocket.*;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.commands.UserMakeMoveCommand;
import websocket.messages.LoadGameMessage;
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
        Session session = ctx.session;
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                UserMakeMoveCommand newCommand = new Gson().fromJson(ctx.message(), UserMakeMoveCommand.class);
                makeMove(command.getGameID(), newCommand.getMove());
            }
            else {
                switch (command.getCommandType()) {
                    case LEAVE -> leave(command.getGameID());
                    case RESIGN -> resign(command.getGameID());
                    case CONNECT -> connect(command.getGameID());
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void makeMove(int gameID, ChessMove move ) throws IOException{
        System.out.println("This is a makeMove command");
    }

    private void leave(int gameID) throws IOException{
        System.out.println("This is a leave command");
    }

    private void resign(int gameID) throws IOException{
        System.out.println("This is a resign command");
    }

    private void connect(int gameID) throws IOException{
        System.out.println("This is a connect command");
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    private void updateGames(int gameID, ChessGame game) throws Exception {
        try {
            var notification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.broadcast(gameID, null, notification);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private void respond(Session session){

    }

    private boolean validUser(String authToken){
        try {
            AuthData data = bundle.authDAO.getAuthFromToken(authToken);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    private boolean canAct(GameData game, AuthData data){
        return game.whiteUsername().equals(data.username()) || game.blackUsername().equals(data.username());
    }

}