package dataaccess.memorydaos;

import dataaccess.daointerfaces.AuthDAO;
import model.AuthData;
import java.util.HashMap;

import java.util.UUID;



public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String,String> tokens;

    public MemoryAuthDAO(){
        tokens = new HashMap<>();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String createAuth(String username){
        String token = generateToken();
        tokens.put(token,username);
        return token;
    }

    @Override
    public void deleteAuth(AuthData data){
        tokens.remove(data.authToken());
    }

    @Override
    public AuthData getAuthFromToken(String authToken) {
        if(tokens.containsKey(authToken)){
            return new AuthData(authToken,tokens.get(authToken));
        }
        return null;
    }

    @Override
    public void clear(){
        tokens.clear();
    }
}
