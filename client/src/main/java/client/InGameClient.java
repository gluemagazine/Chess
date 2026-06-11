package client;

import chess.ChessGame;
import ui.BoardPrinter;
import websocket.messages.ServerMessage;

public class InGameClient implements ServerMessageObserver{
    BoardPrinter printer = new BoardPrinter();
    public InGameClient(ChessGame game, ChessGame.TeamColor color){
        printGame(game,color);
    }

    @Override
    public void notify(ServerMessage message) {
        System.out.println(message);
    }

    private void printGame(ChessGame game, ChessGame.TeamColor color){
        printer.printChessBoard(game,color);
    }
}
