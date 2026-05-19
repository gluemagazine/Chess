package dataaccess;

import model.AuthData;
import java.util.HashMap;

import java.util.UUID;



public class MemoryAuthDAO implements AuthDAO{

    private HashMap<String,String> tokens;

    public MemoryAuthDAO(){
        tokens = new HashMap<>();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String createAuth(String username){
        String token = generateToken();
        tokens.put(token,username);
        return token;
    }

    public void deleteAuth(AuthData data){
        tokens.remove(data.authToken());
    }


    public AuthData getAuthFromToken(String authToken) {
        if(tokens.containsKey(authToken)){
            return new AuthData(authToken,tokens.get(authToken));
        }
        return null;
    }

    public void clear(){
        tokens.clear();
    }
}
