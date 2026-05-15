package chess.movegenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        return moveHelper(board,pos,false);
    }


    @Override
    public Collection<ChessMove> getTheoreticalMoves(ChessBoard board, ChessPosition pos) {
        return moveHelper(board,pos,true);
    }

    public Collection<ChessMove> moveHelper(ChessBoard board, ChessPosition pos,boolean theoretical) {
        ArrayList<ChessMove> validPositions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        ChessPosition pesant = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? board.getWhiteEnPasant() : board.getBlackEnPasant();

        int[][] validAttacks;
        int[][] validMoves;
        boolean canDouble = false;
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            validAttacks = new int[][] {{1,1},{1,-1}};
            if(pos.getRow() == 2){
                validMoves = new int[][] {{1,0},{2,0}};
                canDouble = true;
            }
            else{
                validMoves = new int[][] {{1,0}};
            }
        }
        else{
            validAttacks = new int[][] {{-1,1},{-1,-1}};
            if(pos.getRow() == 7){
                validMoves = new int[][] {{-1,0},{-2,0}};
                canDouble = true;
            }
            else{
                validMoves = new int[][] {{-1,0}};
            }
        }

        if(!theoretical){
            for(var dir : validAttacks){
                ChessPosition newPos = getPosFromVector(pos,dir,1);
                if(!validPosition(board,newPos,piece)){
                    continue;
                }
                if(board.getPiece(newPos) == null){
                    if (pesant.equals(newPos)){
                        ChessMove move = new ChessMove(pos,newPos,null);
                        validPositions.add(move);
                    }
                    continue;
                }
                ChessMove move = new ChessMove(pos,newPos,null);
                validPositions.add(move);
            }
        }
        else {
            for(var dir : validAttacks){
                ChessPosition newPos = getPosFromVector(pos,dir,1);
                if(board.isValid(newPos)){
                    ChessMove move = new ChessMove(pos,newPos,null);
                    validPositions.add(move);
                }
            }
        }

        ChessPosition newPos = getPosFromVector(pos,validMoves[0], 1);
        for(int i = 0; i < 1; i++){
            if(!validPosition(board,newPos,piece)){
                break;
            }
            if(!(board.getPiece(newPos) == null)){
                break;
            }
            ChessMove move = new ChessMove(pos,newPos,null);
            validPositions.add(move);
            if(!canDouble){
                break;
            }
            newPos = getPosFromVector(pos,validMoves[1], 1);
            if(board.getPiece(newPos) == null){
                if(validPosition(board,newPos,piece)) {
                    move = new ChessMove(pos,newPos,null);
                    validPositions.add(move);
                }
            }
        }

        ArrayList<ChessMove> promotedPositions = new ArrayList<>();

        for (var move : validPositions){
            if(move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8){
                promotedPositions.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.BISHOP));
                promotedPositions.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.ROOK));
                promotedPositions.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                promotedPositions.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.QUEEN));
            }
            else{
                promotedPositions.add(move);
            }
        }

        return promotedPositions;
    }

}
