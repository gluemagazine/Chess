package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {

    void clear() throws DataAccessException;

    AuthData getAuthFromToken(String authToken) throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    void deleteAuth(AuthData data) throws DataAccessException;
}
