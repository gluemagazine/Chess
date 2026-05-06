package chess.MoveGenerators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveGenerator extends MoveGenerator{
    public Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int[][] dirs = {{2,1},{2,-1},{-2,-1},{-2,1},{1,2},{1,-2},{-1,-2},{-1,2}};
        for (int[] dir : dirs){
            ChessPosition new_pos = getPosFromVector(position,dir,1);
            if(checkValidPosition(board,board.getPiece(position),new_pos)){
                ChessMove move = new ChessMove(position,new_pos,null);
                validMoves.add(move);
            }
        }
        return validMoves;
    }
}
