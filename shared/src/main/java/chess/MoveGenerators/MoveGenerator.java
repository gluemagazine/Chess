package chess.MoveGenerators;
import chess.*;

import java.util.Collection;

public abstract class MoveGenerator {

    public abstract Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece);

    public ChessPosition getPosition(ChessPosition start, int[] dir,int magnitude){
        return new ChessPosition(start.getRow() + dir[0] * magnitude, start.getColumn() + dir[1] * magnitude);
    }

    public ChessPosition getDiagonal(ChessPosition start, int[] slope,int magnitude){
        return getPosition(start,slope,magnitude);
    }

    public boolean pawnGetAndCheck(ChessPosition destination,ChessBoard board,ChessPiece self){
        if (getAndCheck(destination,board,self)){
            if(board.getPiece(destination) == null){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean getAndCheck(ChessPosition destination,ChessBoard board,ChessPiece self){
        if (!board.isValid(destination)){
            return false;
        }
        ChessPiece at = board.getPiece(destination);
        if(at == null){
            return true;
        }
        return at.getTeamColor() != self.getTeamColor();
    }

}
