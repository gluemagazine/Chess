package chess.MoveGenerators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
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

        for(var dir : validAttacks){
            ChessPosition new_pos = getPosFromVector(pos,dir,1);
            if(validPosition(board,new_pos,piece)){
                if(board.getPiece(new_pos) == null){
                    if (pesant.equals(new_pos)){
                        ChessMove move = new ChessMove(pos,new_pos,null);
                        valid_positions.add(move);
                    }
                    continue;
                }
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
            }
        }


        ChessPosition new_pos = getPosFromVector(pos,validMoves[0], 1);
        if(validPosition(board,new_pos,piece)){
            if(board.getPiece(new_pos) == null){
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
                if(canDouble){
                    new_pos = getPosFromVector(pos,validMoves[1], 1);
                    if(board.getPiece(new_pos) == null){
                        if(validPosition(board,new_pos,piece)) {
                            move = new ChessMove(pos,new_pos,null);
                            valid_positions.add(move);
                        }
                    }
                }
            }
        }
        ArrayList<ChessMove> promotedPositions = new ArrayList<>();

        for (var move : valid_positions){
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


    @Override
    public Collection<ChessMove> getTheoreticalMoves(ChessBoard board, ChessPosition pos) {
        ArrayList<ChessMove> valid_positions = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
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

        for(var dir : validAttacks){
            ChessPosition new_pos = getPosFromVector(pos,dir,1);
            if(board.isValid(new_pos)){
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
            }
        }


        ChessPosition new_pos = getPosFromVector(pos,validMoves[0], 1);
        if(validPosition(board,new_pos,piece)){
            if(board.getPiece(new_pos) == null){
                ChessMove move = new ChessMove(pos,new_pos,null);
                valid_positions.add(move);
                if(canDouble){
                    new_pos = getPosFromVector(pos,validMoves[1], 1);
                    if(board.getPiece(new_pos) == null){
                        if(validPosition(board,new_pos,piece)) {
                            move = new ChessMove(pos,new_pos,null);
                            valid_positions.add(move);
                        }
                    }
                }
            }
        }
        ArrayList<ChessMove> promotedPositions = new ArrayList<>();

        for (var move : valid_positions){
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
