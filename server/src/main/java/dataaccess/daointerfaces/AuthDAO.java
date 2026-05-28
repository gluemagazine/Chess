package dataaccess.daointerfaces;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAO {

    void clear() throws DataAccessException;

    AuthData getAuthFromToken(String authToken) throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    void deleteAuth(AuthData data) throws DataAccessException;
}
