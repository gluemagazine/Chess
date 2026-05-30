package service;


import dataaccess.daointerfaces.AuthDAO;
import dataaccess.daointerfaces.UserDAO;
import exceptions.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

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
        if (!(BCrypt.checkpw(request.password(), result.password()))){
            throw new DataAccessException("Error: unauthorized");
        }
        String token = auth.createAuth(result.username());

        return new LoginResult(result.username(),token);
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        UserData result = users.getUser(request.username());
        if(result != null){
            throw new AlreadyTakenException("Error: Username already taken");
        }

        if(request.username() == null || request.password() == null || request.email() == null){
            throw new BadCredentialsException("Error: bad request");
        }
        String hashed = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        users.createUser(new UserData(request.username(),hashed, request.email()));

        String token = auth.createAuth(request.username());

        return new RegisterResult(request.username(),token);
    }

    public void logout(LogoutRequest request) throws DataAccessException{
        AuthData result = auth.getAuthFromToken(request.authToken());

        if (result == null){
            throw new InvalidAuthException("Error: unauthorized");
        }

        auth.deleteAuth(result);
    }

    public void clear() throws DataAccessException{
        users.clear();
    }
}
