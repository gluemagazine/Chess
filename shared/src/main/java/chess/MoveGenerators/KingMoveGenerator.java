package chess.MoveGenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        int[][] dirs = {{-1,0},{0,1},{1,0},{0,-1},{-1,-1},{-1,1},{1,-1},{1,1}};
        for(var dir: dirs){
            ChessPosition new_pos = getPosFromVector(pos,dir,1);
            if(validPosition(board,new_pos,piece)){
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
            }
        }
        return valid_positions;
    }
}
