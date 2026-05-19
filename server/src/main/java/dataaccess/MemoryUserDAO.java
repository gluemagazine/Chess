package dataaccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<String, UserData> users;

    public MemoryUserDAO(){
        users = new HashMap<>();
    }

    public void createUser(UserData userData) {
        users.put(userData.username(),userData);
    }

    public UserData getUser(String username) {
        if(users.containsKey(username)){
            return users.get(username);
        }
        return null;
    }

    public void clear(){
        users.clear();
    }
}
