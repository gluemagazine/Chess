package chess.MoveGenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        int[][] dirs = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,-2},{-1,2}};
        for(var dir: dirs){
            ChessPosition new_pos = getPosFromVector(pos,dir,1);
            if(validPosition(board,new_pos,piece)){
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
            }
        }
        return valid_positions;
    }


    @Override
    public Collection<ChessMove> getTheoreticalMoves(ChessBoard board, ChessPosition pos) {
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        int[][] dirs = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,-2},{-1,2}};
        for(var dir: dirs){
            ChessPosition new_pos = getPosFromVector(pos,dir,1);
            if(board.isValid(new_pos)){
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
            }
        }
        return valid_positions;
    }
}
