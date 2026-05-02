package chess.MoveGenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        int[][] dirs = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
        for (int[] dir : dirs) {
            for (int i = 0; i < 8; i++) {
                ChessPosition next = getPosition(myPosition, dir, i + 1);
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
