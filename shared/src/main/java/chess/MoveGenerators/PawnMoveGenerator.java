package chess.MoveGenerators;
import chess.*;

import java.util.Collection;
import java.util.List;

public class PawnMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        return List.of();
    }
}
