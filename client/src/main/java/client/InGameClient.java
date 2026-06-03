package client;

import chess.ChessGame;

public class InGameClient {
    BoardPrinter printer = new BoardPrinter();
    public InGameClient(ChessGame game, ChessGame.TeamColor color){
        printer.printChessBoard(game,color);
    }
}
