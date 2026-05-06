package chess.MoveGenerators;

import chess.*;

import java.util.Collection;
abstract
public class MoveGenerator {
    abstract public Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position);

    public boolean checkValidPosition(ChessBoard board, ChessPiece piece, ChessPosition pos) {
        if(!board.isValid(pos)){
            return false;
        }

        ChessPiece at = board.getPiece(pos);
        if(at == null) {
            return true;
        }
        if(at.getTeamColor() != piece.getTeamColor()){
            return true;
        }
        return false;
    }

    public ChessPosition getPosFromVector(ChessPosition pos,int[] dir,int magnitude){
        return new ChessPosition(pos.getRow() + dir[0] * magnitude,pos.getColumn() + dir[1] * magnitude);
    }
}
