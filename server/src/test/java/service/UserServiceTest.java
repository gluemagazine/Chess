package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserServiceTest {

    private UserService user;
    private DataService data;

    @BeforeEach
    void createServices(){
        GameDAO games = new MemoryGameDAO();
        AuthDAO auth = new MemoryAuthDAO();
        UserDAO users = new MemoryUserDAO();
        user = new UserService(users,auth);
        data = new DataService(auth,games,users);
    }

    @Test
    void loginSuccessful() {
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            user.logout(new LogoutRequest(result.authToken()));
            LoginResult newResult = user.login(new LoginRequest("ExistingUser","password"));
            Assertions.assertNotNull(newResult.authToken());
            Assertions.assertEquals("ExistingUser",newResult.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void loginNonExistentUser(){
        Assertions.assertThrows(DataAccessException.class, ()-> user.login(new LoginRequest("ExistingUser","password")));
    }

    @Test
    void loginBadCredentials(){
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            user.logout(new LogoutRequest(result.authToken()));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()-> user.login(new LoginRequest("ExistingUser","badPassword")));
    }

    @Test
    void loginBadRequest(){
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            user.logout(new LogoutRequest(result.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(BadCredentialsException.class, ()-> user.login(new LoginRequest(null,"null")));
    }

    @Test
    void registerSuccessful() {
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            Assertions.assertNotNull(result.authToken());
            Assertions.assertEquals("ExistingUser",result.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerExistingUser(){
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            Assertions.assertNotNull(result.authToken());
            Assertions.assertEquals("ExistingUser",result.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(AlreadyTakenException.class, ()->user.register(new RegisterRequest("ExistingUser","password","example")));
    }

    @Test
    void registerBadRequest(){
        Assertions.assertThrows(BadCredentialsException.class, ()->user.register(new RegisterRequest(null,"password","example")));
        Assertions.assertThrows(BadCredentialsException.class, ()->user.register(new RegisterRequest("ExistingUser","password",null)));
        Assertions.assertThrows(BadCredentialsException.class, ()->user.register(new RegisterRequest("ExistingUser",null,"example")));
    }

    @Test
    void logout() {
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            user.logout(new LogoutRequest(result.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void clear() {
        try {
            RegisterResult result = user.register(new RegisterRequest("ExistingUser","password","example"));
            Assertions.assertNotNull(result.authToken());
            Assertions.assertEquals("ExistingUser",result.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        user.clear();
        data.clear();
        Assertions.assertThrows(DataAccessException.class, ()-> user.login(new LoginRequest("ExistingUser","password")));
    }
}