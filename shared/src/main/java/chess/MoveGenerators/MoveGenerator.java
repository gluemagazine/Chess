package chess.MoveGenerators;

import chess.*;

import java.util.Collection;
abstract
public class MoveGenerator {
    abstract public Collection<ChessPiece> generateMoves(ChessBoard board, ChessPosition position);

}
