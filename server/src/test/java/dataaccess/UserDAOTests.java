package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class UserDAOTests {
    DataAccessBundle bundle  = new DataAccessBundle(false);

    @AfterEach
    @BeforeEach
    void clearAuthData(){
        try {
            bundle.authDAO.clear();
            bundle.userDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



}
