package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class UserMakeMoveCommand extends UserGameCommand{
    private final ChessMove move;
    private final ChessGame.TeamColor color;
    public UserMakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move, ChessGame.TeamColor color) {
        super(commandType, authToken, gameID);
        this.move = move;
        this.color = color;
    }

    public ChessMove getMove(){
        return move;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }
}
