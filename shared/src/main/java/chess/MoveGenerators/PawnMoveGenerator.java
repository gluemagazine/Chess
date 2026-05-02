package chess.MoveGenerators;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;




public class PawnMoveGenerator extends MoveGenerator{

    ArrayList<ChessPiece> promotions;

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        int[][] slopes = {{1,0}, {-1,0}};
        int[][] white_diagonals = { {1,1}, {1,-1}};
        int[][] black_diagonals = {{-1,1},{-1,-1}};
        ChessPosition next;
        switch(piece.getTeamColor()){
            case BLACK:
                next = getPosition(myPosition,slopes[1],1);
                if(!board.isValid(next)) {
                    break;
                }
                ChessPiece at = board.getPiece(next);
                if (at == null){
                    valid_moves.add(new ChessMove(myPosition, next,null));
                    if (myPosition.getRow() == 7) {
                        next = getPosition(myPosition, slopes[1], 2);
                        if (board.isValid(next)) {
                            valid_moves.add(new ChessMove(myPosition, next, null));
                        }
                    }
                }
                for(int[] diagonal : black_diagonals){
                    next = getDiagonal(myPosition,diagonal,1);
                    boolean result = getAndCheck(next,board,piece);
                    if(result){
                        valid_moves.add(new ChessMove(myPosition,next,null));
                    }
                }

                break;
            case WHITE:
                next = getPosition(myPosition,slopes[0],1);
                if(!board.isValid(next)){
                    break;
                }
                valid_moves.add(new ChessMove(myPosition, next,null));
                if (myPosition.getRow() == 2){
                    next = getPosition(myPosition,slopes[1],2);
                    if(board.isValid(next)){
                        valid_moves.add(new ChessMove(myPosition, next,null));
                    }
                }
                break;
        }
        return valid_moves;
    }
}
