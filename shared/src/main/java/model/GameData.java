package model;

import chess.ChessGame;

public record GameData(int ID, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
}
