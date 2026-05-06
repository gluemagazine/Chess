package chess.MoveGenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveGenerator extends MoveGenerator{
    public Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int[][] validPawnMoves;
        int[][] validAttacks;
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE){
            validAttacks = new int[][] {{1,1},{1,-1}};
            if (position.getRow() == 2){
                validPawnMoves = new int[][] {{1,0},{2,0}};
            }
            else{
                validPawnMoves = new int[][] {{1,0}};
            }
        }
        else{
            validAttacks = new int[][] {{-1,1},{-1,-1}};
            if (position.getRow() == 7){
                validPawnMoves = new int[][] {{-1,0},{-2,0}};
            }
            else{
                validPawnMoves = new int[][] {{-1,0}};
            }
        }
        for (int[] dir : validPawnMoves) {
            ChessPosition new_pos = getPosFromVector(position,dir,1);
            if(checkValidPosition(board,board.getPiece(position),new_pos)){
                ChessMove move = new ChessMove(position,new_pos,null);
                if(board.getPiece(new_pos) != null){
                    break;
                }
                validMoves.add(move);
            }
            else{
                break;
            }
        }
        for(int[] dir: validAttacks){
            ChessPosition new_pos = getPosFromVector(position,dir,1);
            if(checkValidPosition(board,board.getPiece(position),new_pos)){
                ChessMove move = new ChessMove(position,new_pos,null);
                if(board.getPiece(new_pos) == null){
                    continue;
                }
                validMoves.add(move);
            }
        }
        ArrayList<ChessMove> newValidMoves = new ArrayList<>();
        for (var move : validMoves){
            if (move.getEndPosition().getRow() == 8 ||move.getEndPosition().getRow() == 1){
                newValidMoves.add(new ChessMove(position,move.getEndPosition(), ChessPiece.PieceType.QUEEN));
                newValidMoves.add(new ChessMove(position,move.getEndPosition(), ChessPiece.PieceType.BISHOP));
                newValidMoves.add(new ChessMove(position,move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                newValidMoves.add(new ChessMove(position,move.getEndPosition(), ChessPiece.PieceType.ROOK));
            }
            else{
                newValidMoves.add(move);
            }
        }

        return newValidMoves;
    }
}
