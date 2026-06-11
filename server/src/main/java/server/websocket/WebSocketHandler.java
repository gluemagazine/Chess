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
import websocket.messages.ErrorMessage;
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
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                UserMakeMoveCommand newCommand = new Gson().fromJson(ctx.message(), UserMakeMoveCommand.class);
                makeMove(newCommand.getGameID(), newCommand.getMove(), newCommand.getAuthToken() ,ctx.session);
            }
            else {
                switch (command.getCommandType()) {
                    case LEAVE -> leave(command.getGameID(),command.getAuthToken(),ctx.session);
                    case RESIGN -> resign(command.getGameID(),command.getAuthToken(),ctx.session);
                    case CONNECT -> connect(command.getGameID(),command.getAuthToken(),ctx.session);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            try{
                respond(ctx.session,new ErrorMessage("Error: an error occurred while processing your request: " + ex.getMessage()));
            } catch (IOException e) {
                System.out.println("there was an error trying to respond to the error");
            }
        }
    }

    private void makeMove(int gameID, ChessMove move, String authToken, Session session) throws IOException{
        System.out.println("This is a makeMove command");

        AuthData data = validateAndGetUser(authToken);
        if(data == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }

        respond(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR));
    }

    private void leave(int gameID, String authToken, Session session) throws IOException{
        System.out.println("This is a leave command");

        AuthData data = validateAndGetUser(authToken);
        if(data == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }
    }

    private void resign(int gameID, String authToken, Session session) throws IOException{
        System.out.println("This is a resign command");

        AuthData data = validateAndGetUser(authToken);
        if(data == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }
    }

    private void connect(int gameID, String authToken, Session session) throws IOException{
        System.out.println("This is a connect command");

        AuthData authData = validateAndGetUser(authToken);
        if(authData == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }

        GameData gameData = getGameData(gameID);
        if(gameData == null){
            respond(session, new ErrorMessage("Error: invalid game ID"));
        }


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

    private void respond(Session session, ServerMessage message) throws IOException {
        String msg = message.toString();
        session.getRemote().sendString(msg);
    }

    private AuthData validateAndGetUser(String authToken){
        AuthData data;
        try {
            data = bundle.authDAO.getAuthFromToken(authToken);
            if(data == null){
                return null;
            }
            if(data.username() == null){
                return null;
            }
        } catch (DataAccessException e) {
            return null;
        }
        return data;
    }

    private GameData getGameData(int gameID){
        GameData data;
        try {
            data = bundle.gameDAO.getGame(String.valueOf(gameID));
        } catch (DataAccessException e) {
            return null;
        }
        return data;
    }

    private boolean canAct(GameData game, AuthData data){
        return game.whiteUsername().equals(data.username()) || game.blackUsername().equals(data.username());
    }

}