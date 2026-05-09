package chess.MoveGenerators;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

abstract
public class MoveGenerator {
    abstract public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos);
    abstract public Collection<ChessMove> getTheoreticalMoves(ChessBoard board, ChessPosition pos);

    public Collection<ChessMove> getMovesFromDirs(ChessBoard board, ChessPosition pos, int[][] dirs, int reach){
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        for(var dir: dirs){
            for(int i = 1; i < reach + 1; i ++){
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

    public Collection<ChessMove> getTheoreticalFromDirs(ChessBoard board, ChessPosition pos, int[][] dirs, int reach){
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        for(var dir: dirs){
            for(int i = 1; i < reach + 1; i ++){
                ChessPosition new_pos = getPosFromVector(pos,dir,i);
                if(board.isValid(new_pos)){
                    if( board.getPiece(new_pos) == null){
                        valid_positions.add(new ChessMove(pos,new_pos,null));
                    }
                    else {
                        if(board.getPiece(new_pos).getTeamColor() == piece.getTeamColor()){
                            break;
                        }
                        valid_positions.add(new ChessMove(pos,new_pos,null));
                    }
                }
                else{
                    break;
                }
            }
        }
        return valid_positions;
    }

    public boolean validPosition(ChessBoard board, ChessPosition pos, ChessPiece piece){
        if(!board.isValid(pos)){
            return false;
        }
        ChessPiece at = board.getPiece(pos);
        if(at == null){
            return true;
        }
        return at.getTeamColor() != piece.getTeamColor();
    }

    public ChessPosition getPosFromVector(ChessPosition pos, int[] dir, int magnitude){
        return new ChessPosition(pos.getRow() + dir[0] * magnitude, pos.getColumn() + dir[1] * magnitude);
    }
}
