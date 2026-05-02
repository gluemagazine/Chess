package chess.MoveGenerators;
import chess.*;

import java.util.Collection;

public abstract class MoveGenerator {

    public abstract Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece);


    public ChessPosition getDiagonal(ChessPosition start, int[] slope,int magnitude){
        return new ChessPosition(start.getRow() + slope[0] * magnitude, start.getColumn() + slope[1] * magnitude);
    }

}
