package websocket.commands;

import chess.ChessGame;

public class UserJoinGameCommand extends UserGameCommand{
    private final ChessGame.TeamColor color;

    public UserJoinGameCommand(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
        color = null;
    }

    public UserJoinGameCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(commandType, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }
}
