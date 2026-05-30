package client;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.List;

import ui.EscapeSequences.*;

public class InGameClient {

    private void printChessBoard(ChessGame game, ChessGame.TeamColor color){
        String[] letters = {"a","b","c","d","e","f","g","h"};
        ArrayList<String> reversible = (ArrayList<String>) List.of(letters);

        if (color == ChessGame.TeamColor.BLACK){
            reversible = (ArrayList<String>) reversible.reversed();
        }
    }
}
