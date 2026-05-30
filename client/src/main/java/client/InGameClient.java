package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class InGameClient {

    public InGameClient(ChessGame game, ChessGame.TeamColor color){
        printChessBoard(game,color);
    }

    private void printChessBoard(ChessGame game, ChessGame.TeamColor color){
        String[] letters = {"\u2003a","\u2003b","\u2003c","\u2003d","\u2003e","\u2003f","\u2003g","\u2003h"};
        int starting_number = 8;
        int increment = -1;
        StringBuilder topRow = new StringBuilder().append(" ");
        for(var letter: letters){
            topRow.append(letter).append(" ");
        }

        if (color == ChessGame.TeamColor.BLACK){
            topRow = topRow.reverse();
            starting_number = 1;
            increment = 1;
        }
        System.out.println(SET_BG_COLOR_BLACK + topRow);
        for(int i = 0; i < 8; i++){
            System.out.print(SET_BG_COLOR_BLACK + starting_number);
            for(int j = 0; j < 8; j ++){
                System.out.print(getPiece(game.getBoard(),getPosition(i,j,color)));
            }
            System.out.println(SET_BG_COLOR_BLACK + starting_number );
            starting_number += increment;
        }


        System.out.println(SET_BG_COLOR_BLACK + topRow + RESET_BG_COLOR);

    }

    private ChessPosition getPosition(int i, int j, ChessGame.TeamColor color){
        if(color == ChessGame.TeamColor.WHITE){
            return new ChessPosition(i+1,j+1);
        }
        return new ChessPosition(8-i,8-j);
    }


    private String getPiece(ChessBoard board, ChessPosition position){
        ChessPiece piece = board.getPiece(position);
        if(piece == null){
            return EMPTY;
        }

        String result =  EMPTY;

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            result = switch(piece.getPieceType()){
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            result = switch(piece.getPieceType()){
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }


        return result;
    }
}
