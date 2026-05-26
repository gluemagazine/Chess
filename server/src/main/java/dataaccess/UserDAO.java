package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
