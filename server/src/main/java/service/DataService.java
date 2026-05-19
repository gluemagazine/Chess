package service;

import dataaccess.AuthDAO;

public class DataService {
    private final AuthDAO auth;
    public DataService(AuthDAO auth){
        this.auth = auth;
    }

    public void clear(){
        auth.clear();
    }
}
