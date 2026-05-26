package service;


import dataaccess.*;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadCredentialsException;
import dataaccess.Exceptions.DataAccessException;
import dataaccess.Exceptions.InvalidAuthException;
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
        if(request.username() == null || request.password() == null){
            throw new BadCredentialsException("Error: bad request");
        }
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

        if(request.username() == null || request.password() == null || request.email() == null){
            throw new BadCredentialsException("Error: bad request");
        }

        users.createUser(new UserData(request.username(),request.password(), request.email()));

        String token = auth.createAuth(request.username());

        return new RegisterResult(request.username(),token);
    }

    public void logout(LogoutRequest request) throws Exception{
        AuthData result = auth.getAuthFromToken(request.authToken());

        if (result == null){
            throw new InvalidAuthException("Error: unauthorized");
        }

        auth.deleteAuth(result);
    }

    public void clear(){
        try {
            users.clear();
        } catch (DataAccessException _) {

        }
    }

}
