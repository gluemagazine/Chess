package chess.MoveGenerators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        int[][] slopes = {{1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {-2, -1}, {2, -1},{2, 1}, {-2, 1}};
        for (int[] slope : slopes) {
            ChessPosition next = getDiagonal(myPosition, slope, 1);
            if (getAndCheck(next,board,piece)){
                valid_moves.add(new ChessMove(myPosition, next, null));
            }
        }

        return valid_moves;
    }
}
