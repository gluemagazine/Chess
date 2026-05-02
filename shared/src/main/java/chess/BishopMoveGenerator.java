package chess;
import java.util.List;
import java.util.ArrayList;

public class BishopMoveGenerator extends MoveGenerator{
    @Override
    public List<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        valid_moves.add(new ChessMove(myPosition,new ChessPosition(0,0),null));
        for(int i = 0; i < 8; i++){

        }
        return valid_moves;
    }
}
