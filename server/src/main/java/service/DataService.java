package service;

import dataaccess.AuthDAO;
import dataaccess.Exceptions.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DataService {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO users;
    public DataService(AuthDAO auth, GameDAO games, UserDAO users){
        this.auth = auth;
        this.games = games;
        this.users = users;
    }

    public void clear(){
        try {
            auth.clear();
        } catch( DataAccessException _){

        }
    }
}
