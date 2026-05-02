package chess.MoveGenerators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        int[][] slopes = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1},{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
        for (int[] slope : slopes) {
            for (int i = 0; i < 8; i++) {
                ChessPosition next = getDiagonal(myPosition, slope, i + 1);
                if (!board.isValid(next)) {
                    break;
                }
                ChessPiece at = board.getPiece(next);
                if (at == null) {
                    valid_moves.add(new ChessMove(myPosition, next, null));
                } else if (at.getTeamColor() == piece.getTeamColor()) {
                    break;
                } else {
                    valid_moves.add(new ChessMove(myPosition, next, null));
                    break;
                }
            }
        }

        return valid_moves;
    }
}
