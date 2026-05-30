package service;

import dataaccess.*;
import dataaccess.daointerfaces.AuthDAO;
import dataaccess.daointerfaces.GameDAO;
import dataaccess.daointerfaces.UserDAO;
import exceptions.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DataServiceTest {

    private DataService data;


    @BeforeEach
    void createServices(){
        DataAccessBundle bundle = new DataAccessBundle(false);
        UserDAO users = bundle.userDAO;
        AuthDAO auth = bundle.authDAO;
        GameDAO games = bundle.gameDAO;
        data = new DataService(auth,games,users);
    }

    @Test
    void clear() {
        try {
            data.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}