package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DataServiceTest {

    private DataService data;


    @BeforeEach
    void createServices(){
        UserDAO users = new MySQLUserDAO();
        AuthDAO auth = new MySQLAuthDAO();
        GameDAO games = new MySQLGameDAO();
        data = new DataService(auth,games,users);
    }

    @Test
    void clear() {
        data.clear();
    }
}