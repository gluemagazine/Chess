package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    public void printChessBoard(ChessGame game, ChessGame.TeamColor color){
        String[] letters = {"a","b","c","d","e","f","g","h"};
        String topRow = buildRow(letters,"\u2003 ","\u2003\u2003",color == ChessGame.TeamColor.BLACK) + " ";

        System.out.println(SET_BG_COLOR_BLACK + topRow + RESET_BG_COLOR);
        for(int i = 0; i < 8; i++){
            System.out.println(buildColoredRow(i,game,color));
        }

        System.out.println(SET_BG_COLOR_BLACK + topRow + RESET_BG_COLOR);
    }

    public void printHighlighted(ChessGame game, ChessGame.TeamColor color, ArrayList<ChessPosition> toHighlight){
        String[] letters = {"a","b","c","d","e","f","g","h"};
        String topRow = buildRow(letters,"\u2003 ","\u2003\u2003",color == ChessGame.TeamColor.BLACK) + " ";

        System.out.println(SET_BG_COLOR_BLACK + topRow + RESET_BG_COLOR);
        for(int i = 0; i < 8; i++){
            System.out.println(buildHighlightedRow(i,game,color,toHighlight));
        }

        System.out.println(SET_BG_COLOR_BLACK + topRow + RESET_BG_COLOR);
    }

    private String getPiece(ChessBoard board, ChessPosition position){
        ChessPiece piece = board.getPiece(position);
        if(piece == null){
            return EMPTY;
        }

        String result =  EMPTY;

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            result = switch(piece.getPieceType()){
                case KING -> SET_TEXT_COLOR_BLACK + WHITE_KING;
                case QUEEN -> SET_TEXT_COLOR_BLACK + WHITE_QUEEN;
                case BISHOP -> SET_TEXT_COLOR_BLACK + WHITE_BISHOP;
                case KNIGHT -> SET_TEXT_COLOR_BLACK + WHITE_KNIGHT;
                case ROOK -> SET_TEXT_COLOR_BLACK + WHITE_ROOK;
                case PAWN -> SET_TEXT_COLOR_BLACK + WHITE_PAWN;
            };
        } else if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            result = switch(piece.getPieceType()){
                case KING -> SET_TEXT_COLOR_BLACK + BLACK_KING;
                case QUEEN -> SET_TEXT_COLOR_BLACK + BLACK_QUEEN;
                case BISHOP -> SET_TEXT_COLOR_BLACK + BLACK_BISHOP;
                case KNIGHT -> SET_TEXT_COLOR_BLACK + BLACK_KNIGHT;
                case ROOK -> SET_TEXT_COLOR_BLACK + BLACK_ROOK;
                case PAWN -> SET_TEXT_COLOR_BLACK + BLACK_PAWN;
            };
        }

        return result;
    }

    private String buildRow(String[] items, String inBetween, String ends, boolean reversed){
        StringBuilder builder = new StringBuilder();
        builder.append(ends);
        builder.append(items[0]);
        for(int i = 1; i < items.length; i ++){
            builder.append(inBetween);
            builder.append(items[i]);
        }
        builder.append(ends);
        return (reversed) ? builder.reverse().toString() : builder.toString();
    }

    private String buildColoredRow(int row,ChessGame game,ChessGame.TeamColor color){
        String bgColor = (row % 2 == 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLUE ;
        StringBuilder builder = new StringBuilder();
        int realRow = (color == ChessGame.TeamColor.WHITE) ? 7-row : row;

        builder.append(SET_BG_COLOR_BLACK).append(realRow + 1).append(" ");
        for(int i = 0; i < 8; i ++){
            int realCol = (color == ChessGame.TeamColor.WHITE) ? i : 7-i;
            String piece = getPiece(game.getBoard(),new ChessPosition(realRow+1,realCol+1));
            builder.append(bgColor);
            bgColor = (bgColor.equals(SET_BG_COLOR_BLUE)) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLUE;
            builder.append(piece);
        }
        builder.append(RESET_TEXT_COLOR).append(SET_BG_COLOR_BLACK).append(" ").append(realRow + 1).append(" ").append(RESET_BG_COLOR);
        return builder.toString();
    }

    private String buildHighlightedRow(int row,ChessGame game,ChessGame.TeamColor color, ArrayList<ChessPosition> toHighlight){
        String bgColor = (row % 2 == 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLUE ;
        StringBuilder builder = new StringBuilder();
        int realRow = (color == ChessGame.TeamColor.WHITE) ? 7-row : row;

        builder.append(SET_BG_COLOR_BLACK).append(realRow + 1).append(" ");
        for(int i = 0; i < 8; i ++){
            int realCol = (color == ChessGame.TeamColor.WHITE) ? i : 7-i;
            ChessPosition curr = new ChessPosition(realRow+1,realCol+1);
            String piece = getPiece(game.getBoard(),new ChessPosition(realRow+1,realCol+1));
            if(toHighlight.contains(curr)){
                builder.append((bgColor.equals(SET_BG_COLOR_BLUE) ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN));
            } else{
                builder.append(bgColor);
            }
            bgColor = (bgColor.equals(SET_BG_COLOR_BLUE)) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLUE;
            builder.append(piece);
        }
        builder.append(RESET_TEXT_COLOR).append(SET_BG_COLOR_BLACK).append(" ").append(realRow + 1).append(" ").append(RESET_BG_COLOR);
        return builder.toString();
    }
}
