package chess.MoveGenerators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveGenerator extends MoveGenerator{
    public Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int[][] dirs = {{1,1},{-1,1},{1,-1},{-1,-1},{1,0},{0,1},{0,-1},{-1,0}};
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
