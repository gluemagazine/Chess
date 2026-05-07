package chess.MoveGenerators;
import chess.*;

import java.util.Collection;

abstract
public class MoveGenerator {
    abstract public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos);

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
