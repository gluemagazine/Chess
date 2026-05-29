package client;

import model.*;
import server.ServerFacade;

import ui.EscapeSequences.*;

public class Client {

    public enum clientStates {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    private boolean hasQuit = false;

    String loggedOutHelp =
            """
            register <Username> <Password> <Email> - Creates a new account
            login <Username> <Password> - Log in as an existing user
            quit - playing chess
            help - display all current valid commands""";

    String loggedInHelp =
            """
            create <Name> - creates a game with the given name
            list - lists all currently available games
            join <ID> [WHITE|BLACK] - join the game with the given ID as the specified player
            observe <ID> - join the specified game as an observer
            logout - logs you out
            quit - playing chess
            help - display all current valid commands""";

    String invalidCommand = "Invalid command or arguments, type \"help\" to get a list of valid commands and parameters.";


    private clientStates state;
    private final ServerFacade server;
    private String authToken = null;

    public Client(String url){
        state = clientStates.LOGGED_OUT;
        server = new ServerFacade(url);

        Repl initial = new Repl(loggedOutHelp,this);
    }

    public void processInput(String input){
        String[] params = input.split(" ");
        try {
            switch (params[0]){
                case "login" :
                    if ((params.length == 3)) {
                        login(params[1], params[2]);
                    } else {
                        System.out.println("Not enough or too many arguments given to successfully login");
                    }
                    break;
                case "register" :
                    if ((params.length == 4)) {
                        register(params[1], params[2], params[3]);
                    } else {
                        System.out.println("Not enough or too many arguments given to successfully register");
                    }
                    break;
                case "logout" : logout();

            }
        } catch (Throwable e){
            System.out.println(invalidCommand);
        }
    }

    private boolean validState(clientStates desired){
        return desired == state;
    }

    public void register (String username, String password, String email){
        if(!validState(clientStates.LOGGED_OUT)){
            return;
        }
        try {
            RegisterResult response = server.registerUser(new RegisterRequest(username,password,email));
            if(response.username().equals(username) && response.authToken() != null ){
                authToken = response.authToken();
                System.out.println("Successfully logged in as " + username);
                state = clientStates.LOGGED_IN;
                new Repl(loggedInHelp,this);
            }
        } catch(Throwable e){
            System.out.println("There was an error while registering, make sure" +
                    " to provide a valid username, password, and email address.");
        }
    }

    public void login(String username, String password){
        if(!validState(clientStates.LOGGED_OUT)){
            return;
        }
        try {
            LoginResult response = server.loginUser(new LoginRequest(username,password));
            if(response.username().equals(username) && response.authToken() != null ){
                authToken = response.authToken();
                System.out.println("Successfully logged in as " + username);
                state = clientStates.LOGGED_IN;
                new Repl(loggedInHelp,this);
            }
        } catch(Throwable e){
            System.out.println("There was an error while logging in, make sure" +
                    " to provide a valid username and password.\n" +
                    "If you have not yet registered, please do so");
        }
    }

    public void logout(){
        if(!validState(clientStates.LOGGED_IN)){
            return;
        }
        try {
            server.logout(new LogoutRequest(authToken));
            state = clientStates.LOGGED_OUT;
        } catch(Throwable e){
            System.out.println("There was an error while logging out");
        }
    }

    public clientStates getClientState(){
        return state;
    }

    public boolean getHasQuit(){
        return hasQuit;
    }
    public void setHasQuit(boolean newVal){
        hasQuit = newVal;
    }
}
