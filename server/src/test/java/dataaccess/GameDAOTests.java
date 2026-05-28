package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDAOTests {
    DataAccessBundle bundle  = new DataAccessBundle(false);

    @AfterEach
    @BeforeEach
    void clearAuthData(){
        try {
            bundle.authDAO.clear();
            bundle.gameDAO.clear();
            bundle.userDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameTest(){
        try {
            String result = bundle.gameDAO.createGame("TestGame");
            Assertions.assertEquals("1",result);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void badCreateGame(){
        Assertions.assertThrows(Exception.class,() ->bundle.gameDAO.createGame(null));
    }

    @Test
    void getGameTest(){
        try {
            String result = bundle.gameDAO.createGame("TestGame1");
            Assertions.assertEquals("1",result);
            GameData data = bundle.gameDAO.getGame(result);
            Assertions.assertEquals("TestGame1",data.gameName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void badGetGame(){
        Assertions.assertThrows(Exception.class,() ->bundle.gameDAO.getGame(null));
    }

    @Test
    void listGamesTest(){
        try {
            String result = bundle.gameDAO.createGame("TestGame1");
            Assertions.assertEquals("1",result);
            result = bundle.gameDAO.createGame("TestGame2");
            Assertions.assertEquals("2",result);
            result = bundle.gameDAO.createGame("TestGame3");
            Assertions.assertEquals("3",result);
            ArrayList<GameData> data = bundle.gameDAO.listGames();
            for(int i = 1; i < 4; i ++){
                Assertions.assertEquals("TestGame" + i,data.get(i-1).gameName());
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void badListGames(){
        badCreateGame();
    }

    @Test
    void updateGameTest(){
        try {
            String result = bundle.gameDAO.createGame("TestGame1");
            Assertions.assertEquals("1",result);
            GameData newGameData = new GameData(1,null,null,"NewName",new ChessGame());
            bundle.gameDAO.updateGame("1",newGameData);
            GameData data = bundle.gameDAO.getGame(result);
            Assertions.assertEquals("NewName",data.gameName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void badUpdateGame(){
        try {
            String result = bundle.gameDAO.createGame("TestGame1");
            Assertions.assertEquals("1",result);
            Assertions.assertThrows(Exception.class,() ->bundle.gameDAO.updateGame("1",null));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
