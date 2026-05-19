package service;


import dataaccess.AlreadyTakenException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class UserService {

    UserDAO users;
    AuthDAO auth;

    public UserService(UserDAO users, AuthDAO auth){
        this.users = users;
        this.auth = auth;
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {

        UserData result = users.getUser(request.username());
        if(result == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if (!(request.password().equals(result.password()))){
            throw new DataAccessException("Error: unauthorized");
        }
        String token = auth.createAuth(result.username());

        return new LoginResult(result.username(),token);
    }

    public RegisterResult register(RegisterRequest request) throws Exception {
        UserData result = users.getUser(request.username());
        if(result != null){
            throw new AlreadyTakenException("Error: Username already taken");
        }

        users.createUser(new UserData(request.username(),request.password(), request.email()));

        String token = auth.createAuth(request.username());

        return new RegisterResult(request.username(),token);
    }

    public void clear(){
        users.clear();
    }

}
