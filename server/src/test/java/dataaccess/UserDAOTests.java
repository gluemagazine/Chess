package dataaccess;

import exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDAOTests {
    DataAccessBundle bundle  = new DataAccessBundle(false);

    private final UserData user = new UserData("Username","Password","Email");

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

    @Test
    void createUserTest(){
        try {
            bundle.userDAO.createUser(user);
            UserData result = bundle.userDAO.getUser("Username");
            Assertions.assertEquals("Username",result.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void badCreateUserTest(){
        Assertions.assertThrows(Exception.class,() ->bundle.userDAO.createUser(null));
    }

    @Test
    void getUserTest(){
        try {
            bundle.userDAO.createUser(user);
            UserData result = bundle.userDAO.getUser("Username");
            Assertions.assertEquals("Username",result.username());
            Assertions.assertEquals("Password",result.password());
            Assertions.assertEquals("Email",result.email());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void badGetUserTest(){
        try {
            Assertions.assertNull(bundle.userDAO.getUser(null));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
