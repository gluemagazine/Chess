package chess.MoveGenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class BishopMoveGenerator extends MoveGenerator{
    public Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int[][] dirs = {{1,1},{-1,1},{1,-1},{-1,-1}};
        for (int[] dir : dirs){
            for(int i = 1; i < 9; i++){
                ChessPosition new_pos = getPosFromVector(position,dir,i);
                if(checkValidPosition(board,board.getPiece(position),new_pos)){
                    ChessMove move = new ChessMove(position,new_pos,null);
                    validMoves.add(move);
                    if(board.getPiece(new_pos) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        return validMoves;
    }


}
