package dataaccess;

import model.AuthData;

public class MySQLAuthDAO implements AuthDAO{

    @Override
    public void clear() {

    }

    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {

    }
}
