package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDAOTests {
    private MySQLAuthDAO auth;

    @AfterEach
    @BeforeEach
    void clearAuthData(){
        try {
            auth = new MySQLAuthDAO();
            auth.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void createAuthToken(){
        String token;
        try {
             token = auth.createAuth("Testing123");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(token);
        System.out.println(token);
    }

    @Test
    public void clearAuth(){
        String[] tokens = new String[3];
        try {
            for (int i = 0; i < 3; i ++){
                tokens[i] = auth.createAuth("Testing" + i);
            }
            auth.clear();
            for (int i = 0; i < 3; i ++){
                Assertions.assertNull(auth.getAuthFromToken(tokens[i]));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void getAuthData(){
        String token;
        AuthData data;
        try {
            token = auth.createAuth("Testing123");
             data = auth.getAuthFromToken(token);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(token);
        Assertions.assertNotNull(data);
        System.out.println(token);
        System.out.println(data);
    }

    @Test
    public void deleteAuthData(){
        String token;
        try {
            token = auth.createAuth("Testing123");
            auth.deleteAuth(auth.getAuthFromToken(token));
            Assertions.assertNull(auth.getAuthFromToken(token));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(token);
        System.out.println(token);
    }

}
