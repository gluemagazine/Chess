package chess.MoveGenerators;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        int[][] dirs = {{-1,-1},{-1,1},{1,-1},{1,1}};
        for(var dir: dirs){
            for(int i = 1; i < 9; i ++){
                ChessPosition new_pos = getPosFromVector(pos,dir,i);
                if(validPosition(board,new_pos,piece)){
                    ChessMove move = new ChessMove(pos,new_pos,null);
                    valid_positions.add(move);
                    if(board.getPiece(new_pos) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        return valid_positions;
    }
}
