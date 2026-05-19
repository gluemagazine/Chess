package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();

    AuthData getAuthFromToken(String authToken) throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    void deleteAuth(AuthData data) throws DataAccessException;
}
