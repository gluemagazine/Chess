package service;

import dataaccess.daointerfaces.AuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.daointerfaces.GameDAO;
import dataaccess.daointerfaces.UserDAO;

public class DataService {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO users;
    public DataService(AuthDAO auth, GameDAO games, UserDAO users){
        this.auth = auth;
        this.games = games;
        this.users = users;
    }

    public void clear() throws DataAccessException{
        auth.clear();
    }
}
