package chess;
import java.util.List;

public abstract class MoveGenerator {
    public abstract List<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition);
}
