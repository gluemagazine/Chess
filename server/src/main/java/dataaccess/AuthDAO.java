package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();

    AuthData getAuthFromToken(String authToken);
    String createAuth(String username);
    void deleteAuth(AuthData data);
}
