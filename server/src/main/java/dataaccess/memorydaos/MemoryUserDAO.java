package dataaccess.memorydaos;

import dataaccess.daointerfaces.UserDAO;
import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String, UserData> users;

    public MemoryUserDAO(){
        users = new HashMap<>();
    }

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(),userData);
    }

    @Override
    public UserData getUser(String username) {
        if(users.containsKey(username)){
            return users.get(username);
        }
        return null;
    }

    @Override
    public void clear(){
        users.clear();
    }
}
