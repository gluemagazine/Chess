package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData changeWhite(String newUsername){
        return new GameData(gameID,newUsername,blackUsername,gameName,game);
    }
    public GameData changeBlack(String newUsername){
        return new GameData(gameID,whiteUsername,newUsername,gameName,game);
    }
    public GameData updateGame(ChessGame game){
        return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
    }
}
