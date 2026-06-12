package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessBundle;
import exceptions.DataAccessException;
import io.javalin.websocket.*;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.commands.UserJoinGameCommand;
import websocket.commands.UserMakeMoveCommand;
import websocket.commands.UserResignCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

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
                makeMove(newCommand.getGameID(), newCommand.getMove(), newCommand.getColor(), newCommand.getAuthToken() ,ctx.session);
            }
            else if(command.getCommandType() == UserGameCommand.CommandType.CONNECT){
                UserJoinGameCommand newCommand = new Gson().fromJson(ctx.message(), UserJoinGameCommand.class);
                connect(newCommand.getGameID(), newCommand.getAuthToken(), newCommand.getColor() ,ctx.session);
            }
            else if(command.getCommandType() == UserGameCommand.CommandType.RESIGN){
                UserResignCommand newCommand = new Gson().fromJson(ctx.message(), UserResignCommand.class);
                resign(command.getGameID(),command.getAuthToken(),ctx.session,newCommand.getObserving());
            }
            else {
                switch (command.getCommandType()) {
                    case LEAVE -> leave(command.getGameID(),command.getAuthToken(),ctx.session);
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

    private void makeMove(int gameID, ChessMove move, ChessGame.TeamColor color, String authToken, Session session) throws IOException{
        System.out.println("This is a makeMove command");

        AuthData authData = validateAndGetUser(authToken);
        if(authData == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }

        GameData gameData = getGameData(gameID);
        if(gameData == null){
            respond(session, new ErrorMessage("Error: invalid game ID"));
            return;
        }

        if(!canAct(gameData,authData)){
            respond(session, new ErrorMessage("Error: observers can't act!"));
            return;
        }

        ChessGame game = gameData.game();

        if(game.getGameOver()){
            respond(session, new ErrorMessage("Error: this game is over!"));
            return;
        }

        String target = (game.getTeamTurn() == ChessGame.TeamColor.WHITE) ? gameData.whiteUsername() : gameData.blackUsername();
        if(!authData.username().equals(target)){
            respond(session, new ErrorMessage("Error: it is not your turn!"));
            return;
        }

        NotificationMessage msg = null;
        try {
            game.makeMove(move);
            String username = (game.getTeamTurn() == ChessGame.TeamColor.WHITE) ? gameData.whiteUsername() : gameData.blackUsername();
            if(game.isInCheckmate(game.getTeamTurn())){
                game.setGameOver(true);
                msg = new NotificationMessage(username + " is in checkmate");
            } else if(game.isInCheck(game.getTeamTurn())){
                msg = new NotificationMessage(username + " is in check");
            } else if(game.isInStalemate(game.getTeamTurn())){
                game.setGameOver(true);
                msg = new NotificationMessage(username + " is in a stalemate");
            }
            bundle.gameDAO.updateGame(String.valueOf(gameID),gameData.updateGame(game));
        } catch (InvalidMoveException e) {
            System.out.println(game);
            System.out.println(move);
            System.out.println(game.validMoves(move.getStartPosition()));
            respond(session, new ErrorMessage("Error: invalid move: " + e.getMessage()));
            return;
        } catch (DataAccessException ex){
            respond(session, new ErrorMessage("Error: there was an error actually updating the game"));
            return;
        }
        updateGames(gameID,gameData.game());
        String moveString;
        String startPosition = move.getStartPosition().getLetterMove();
        String endPosition = move.getEndPosition().getLetterMove();

        if(move.getPromotionPiece() != null){
            moveString = authData.username() + " made the move " + startPosition + " to "
                    + endPosition  + " " + move.getPromotionPiece();
        }
        else {
            moveString = authData.username() + " made the move " + startPosition + " to " + endPosition;
        }

        NotificationMessage moveNotice = new NotificationMessage(moveString);
        connections.broadcast(gameID,session,moveNotice);
        if(msg != null){
            connections.broadcast(gameID,null,msg);
        }
    }

    private void leave(int gameID, String authToken, Session session) throws IOException{
        System.out.println("This is a leave command");

        AuthData data = validateAndGetUser(authToken);
        if(data == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }

        GameData gameData = getGameData(gameID);
        if(gameData == null){
            respond(session, new ErrorMessage("Error: invalid game ID"));
            return;
        }
        try {
            if(Objects.equals(gameData.whiteUsername(), data.username())){
                bundle.gameDAO.updateGame(String.valueOf(gameID),gameData.changeWhite(null));
            }
            if(Objects.equals(gameData.blackUsername(), data.username())){
                bundle.gameDAO.updateGame(String.valueOf(gameID),gameData.changeBlack(null));
            }
        } catch (DataAccessException e) {
            throw new IOException(e);
        }


        connections.removeFromGame(gameID,session);

        NotificationMessage message = new NotificationMessage(data.username() + " has left the game");

        connections.broadcast(gameID,session, message);
    }

    private void resign(int gameID, String authToken, Session session, boolean observing) throws IOException{
        System.out.println("This is a resign command");

        AuthData data = validateAndGetUser(authToken);
        if(data == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }

        GameData gameData = getGameData(gameID);
        if(gameData == null){
            respond(session, new ErrorMessage("Error: invalid game ID"));
            return;
        }

        ChessGame game = gameData.game();

        if(game.getGameOver()){
            respond(session, new ErrorMessage("Error: game already over!"));
            return;
        }

        if(observing || (!data.username().equals(gameData.blackUsername()) && !data.username().equals(gameData.whiteUsername()))){
            respond(session,new ErrorMessage("Error: observers cannot resign!"));
            return;
        }

        game.setGameOver(true);

        try {
            bundle.gameDAO.updateGame(String.valueOf(gameID),gameData.updateGame(game));
        } catch (DataAccessException e) {
            throw new IOException(e);
        }

        connections.broadcast(gameID,null,new NotificationMessage(data.username() + " resigned"));
    }

    private void connect(int gameID, String authToken, ChessGame.TeamColor color, Session session) throws IOException{
        System.out.println("This is a connect command");

        AuthData authData = validateAndGetUser(authToken);
        if(authData == null){
            respond(session,new ErrorMessage("Error: unauthorized"));
            return;
        }

        GameData gameData = getGameData(gameID);
        if(gameData == null){
            respond(session, new ErrorMessage("Error: invalid game ID"));
            return;
        }

        connections.addToGame(gameID,session);
        NotificationMessage message;
        if(color == ChessGame.TeamColor.WHITE || Objects.equals(authData.username(), gameData.whiteUsername())){
            message = new NotificationMessage(authData.username() + " joined the game as WHITE");
        }
        else if(color == ChessGame.TeamColor.BLACK || Objects.equals(authData.username(), gameData.blackUsername())){
            message = new NotificationMessage(authData.username() + " joined the game as BLACK");
        }
        else {
            message = new NotificationMessage(authData.username() + " joined the game as an observer");
        }
        connections.broadcast(gameID,session, message);

        respond(session,new LoadGameMessage(gameData.game()));
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    private void updateGames(int gameID, ChessGame game)  throws  IOException{
        var notification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcast(gameID, null, notification);
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