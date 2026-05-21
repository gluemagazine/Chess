package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DataServiceTest {

    private DataService data;


    @BeforeEach
    void createServices(){
        GameDAO games = new MemoryGameDAO();
        AuthDAO auth = new MemoryAuthDAO();
        UserDAO users = new MemoryUserDAO();
        data = new DataService(auth,games,users);
    }

    @Test
    void clear() {
        data.clear();
    }
}