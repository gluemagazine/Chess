package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private final ChessGame game;
    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }
    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
