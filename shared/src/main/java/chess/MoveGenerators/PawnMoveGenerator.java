package chess.MoveGenerators;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;




public class PawnMoveGenerator extends MoveGenerator{

    private boolean checkPromotions(ChessPosition pos,ChessPiece piece){
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return pos.getRow() == 1;
        }
        else{
            return pos.getRow() == 8;
        }
    }

    private void addPromotions(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end){
        moves.add(new ChessMove(start,end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start,end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start,end, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(start,end, ChessPiece.PieceType.BISHOP));
    }

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        int[][] slopes = {{1,0}, {-1,0}};
        int[][] white_diagonals = { {1,1}, {1,-1}};
        int[][] black_diagonals = {{-1,1},{-1,-1}};
        ChessPosition next;
        ChessPiece at;
        switch(piece.getTeamColor()){
            case BLACK:
                next = getPosition(myPosition,slopes[1],1);
                if(!board.isValid(next)) {
                    break;
                }
                at = board.getPiece(next);
                if (at == null){
                    if(checkPromotions(next,piece)){
                        addPromotions(valid_moves,myPosition,next);
                    }
                    else{
                        valid_moves.add(new ChessMove(myPosition,next,null));
                    }
                    if (myPosition.getRow() == 7) {
                        next = getPosition(myPosition, slopes[1], 2);
                        at = board.getPiece(next);
                        if (at == null){
                            if (board.isValid(next)) {
                                if(checkPromotions(next,piece)){
                                    addPromotions(valid_moves,myPosition,next);
                                }
                                else{
                                    valid_moves.add(new ChessMove(myPosition,next,null));
                                }
                            }
                        }
                    }
                }
                for(int[] diagonal : black_diagonals){
                    next = getDiagonal(myPosition,diagonal,1);
                    boolean result = pawnGetAndCheck(next,board,piece);
                    if(result){
                        if(checkPromotions(next,piece)){
                            addPromotions(valid_moves,myPosition,next);
                        }
                        else{
                            valid_moves.add(new ChessMove(myPosition,next,null));
                        }
                    }
                }
                break;
            case WHITE:
                next = getPosition(myPosition,slopes[0],1);
                if(!board.isValid(next)) {
                    break;
                }
                at = board.getPiece(next);
                if (at == null){
                    if(checkPromotions(next,piece)){
                        addPromotions(valid_moves,myPosition,next);
                    }
                    else{
                        valid_moves.add(new ChessMove(myPosition,next,null));
                    }
                    if (myPosition.getRow() == 2) {
                        next = getPosition(myPosition, slopes[0], 2);
                        at = board.getPiece(next);
                        if (at == null){
                            if (board.isValid(next)) {
                                if(checkPromotions(next,piece)){
                                    addPromotions(valid_moves,myPosition,next);
                                }
                                else{
                                    valid_moves.add(new ChessMove(myPosition,next,null));
                                }
                            }
                        }
                    }
                }
                for(int[] diagonal : white_diagonals){
                    next = getDiagonal(myPosition,diagonal,1);
                    boolean result = pawnGetAndCheck(next,board,piece);
                    if(result){
                        if(checkPromotions(next,piece)){
                            addPromotions(valid_moves,myPosition,next);
                        }
                        else{
                            valid_moves.add(new ChessMove(myPosition,next,null));
                        }
                    }
                }
                break;
        }
        return valid_moves;
    }
}
