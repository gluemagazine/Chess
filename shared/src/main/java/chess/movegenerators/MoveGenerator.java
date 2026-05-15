package chess.movegenerators;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

abstract
public class MoveGenerator {
    abstract public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos);
    abstract public Collection<ChessMove> getTheoreticalMoves(ChessBoard board, ChessPosition pos);

    public Collection<ChessMove> getMovesFromDirs(ChessBoard board, ChessPosition pos, int[][] dirs, int reach){
        ArrayList<ChessMove> validPositions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        for(var dir: dirs){
            for(int i = 1; i < reach + 1; i ++){
                ChessPosition newPos = getPosFromVector(pos,dir,i);
                if(validPosition(board,newPos,piece)){
                    ChessMove move = new ChessMove(pos,newPos,null);
                    validPositions.add(move);
                    if(board.getPiece(newPos) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        return validPositions;
    }

    public Collection<ChessMove> getTheoreticalFromDirs(ChessBoard board, ChessPosition pos, int[][] dirs, int reach){
        ArrayList<ChessMove> validPositions = new ArrayList<>();
        for(var dir: dirs){
            for(int i = 1; i < reach + 1; i ++){
                ChessPosition newPos = getPosFromVector(pos,dir,i);
                if(board.isValid(newPos)){
                    ChessMove move = new ChessMove(pos,newPos,null);
                    validPositions.add(move);
                }
                else{
                    break;
                }
            }
        }
        return validPositions;
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
