package client;

import chess.ChessGame;
import exceptions.DataAccessException;
import model.*;

import static ui.EscapeSequences.*;

import java.util.ArrayList;

public class Client {

    public enum ClientStates {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    private boolean hasQuit = false;
    private boolean breakLoop = false;

    private final ArrayList<Integer> listedGames = new ArrayList<>();

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

    String invalidCommand = SET_TEXT_COLOR_RED + "Invalid command or arguments, type \"help\" to get a list of valid commands and parameters.";


    private ClientStates state;
    private final ServerFacade server;
    private String authToken = null;
    private final WebSocketFacade socket;
    private final InGameClient inGameClient;

    public Client(String url){
        state = ClientStates.LOGGED_OUT;
        server = new ServerFacade(url);
        try {
            socket = new WebSocketFacade(url,null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        inGameClient = new InGameClient(socket);
        new Repl(loggedOutHelp,this);
    }

    public void processInput(String input){
        String[] params = input.split(" ");
        try {
            switch (params[0].toLowerCase()){
                case "login" :
                    if ((params.length == 3)) {
                        login(params[1], params[2]);
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Not enough or too many arguments given to successfully login");
                    }
                    break;
                case "register" :
                    if ((params.length == 4)) {
                        register(params[1], params[2], params[3]);
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Not enough or too many arguments given to successfully register");
                    }
                    break;
                case "create" :
                    if ((params.length == 2)) {
                        createGame(params[1]);
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Not enough or too many arguments given to create a game");
                    }
                    break;
                case "join" :
                    if ((params.length == 3)) {
                        if(validID(params[1])){
                            joinGame(Integer.parseInt(params[1]),params[2]);
                        }
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Not enough or too many arguments given to join a game");
                    }
                    break;
                case "observe" :
                    if ((params.length == 2)) {
                        if(validID(params[1])){
                            observeGame(Integer.parseInt(params[1]));
                        }
                        else {
                            System.out.println(SET_TEXT_COLOR_RED + "Please provide a valid game ID");
                        }
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Not enough or too many arguments given to observe a game");
                    }
                    break;
                case "list" :
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for list games");
                        break;
                    }
                    listGames();
                    break;
                case "logout" :
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for logout");
                        break;
                    }
                    logout();
                    break;
                case "quit":
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for quit");
                        break;
                    }
                    setHasQuit(true);
                    break;
                case "help":
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for help");
                        break;
                    }
                    break;
                default:
                    System.out.println(invalidCommand);
            }
        } catch (Throwable e){
            System.out.println(invalidCommand);
        }
    }

    private boolean validState(ClientStates desired){
        return desired == state;
    }

    private void register (String username, String password, String email){
        if(!validState(ClientStates.LOGGED_OUT)){
            System.out.println(invalidCommand);
            return;
        }
        try {
            RegisterResult response = server.registerUser(new RegisterRequest(username,password,email));
            if(response.username().equals(username) && response.authToken() != null ){
                authToken = response.authToken();
                System.out.println("Successfully logged in as " + username);
                state = ClientStates.LOGGED_IN;
                new Repl(loggedInHelp,this);
            }
        } catch(Throwable e){
            System.out.print(SET_TEXT_COLOR_RED);

            if(e.getClass() == DataAccessException.class){
                System.out.println("Response from server: " + sliceOutMessage(e.getMessage()));
            } else {
                System.out.println("There was an error while registering, make sure" +
                        " to provide a valid username, password, and email address.");
            }


        }
    }

    private void login(String username, String password){
        if(!validState(ClientStates.LOGGED_OUT)){
            System.out.println(invalidCommand);
            return;
        }
        try {
            LoginResult response = server.loginUser(new LoginRequest(username,password));
            if(response.username().equals(username) && response.authToken() != null ){
                authToken = response.authToken();
                System.out.println("Successfully logged in as " + username);
                state = ClientStates.LOGGED_IN;
                new Repl(loggedInHelp,this);
            }
        } catch(Throwable e){
            System.out.print(SET_TEXT_COLOR_RED);
            if(e.getClass() == DataAccessException.class){
                System.out.println("Response from server: " + sliceOutMessage(e.getMessage()));
            } else {
                System.out.println("There was an error while logging in, make sure" +
                        " to provide a valid username and password.\n" +
                        "If you have not yet registered, please do so");
            }
        }
    }

    private void logout(){
        if(!validState(ClientStates.LOGGED_IN)){
            System.out.println(invalidCommand);
            return;
        }
        try {
            server.logout(new LogoutRequest(authToken));
            state = ClientStates.LOGGED_OUT;
            breakLoop = true;
        } catch(Throwable e){
            System.out.print(SET_TEXT_COLOR_RED);

            if(e.getClass() == DataAccessException.class){
                System.out.println("Response from server: " + sliceOutMessage(e.getMessage()));
            } else {
                System.out.println("There was an error while logging out");
            }
        }
    }

    private void createGame(String gameName){
        if(!validState(ClientStates.LOGGED_IN)){
            System.out.println(invalidCommand);
            return;
        }
        try {
            CreateGameResult result = server.createGame(new CreateGameRequest(authToken,gameName));
            listedGames.add(Integer.valueOf(result.gameID()));
            System.out.println("Successfully created a game with game name " + gameName + " it is game #" + (listedGames.size()));
        } catch(Throwable e){
            System.out.print(SET_TEXT_COLOR_RED);

            if(e.getClass() == DataAccessException.class){
                System.out.println("Response from server: " + sliceOutMessage(e.getMessage()));
            } else {
                System.out.println("There was an error while creating the game");
            }

            System.out.println("There was an error while creating the game");
        }
    }

    private void joinGame(int id, String color){
        if(!validState(ClientStates.LOGGED_IN)){
            System.out.println(invalidCommand);
            return;
        }
        try {
            ChessGame.TeamColor teamColor;
            if(color.equalsIgnoreCase("WHITE")){
                teamColor = ChessGame.TeamColor.WHITE;
            }
            else if(color.equalsIgnoreCase("BLACK")){
                teamColor = ChessGame.TeamColor.BLACK;
            }
            else {
                System.out.println(SET_TEXT_COLOR_RED + "Invalid team color, make sure the arguments are in the right order");
                return;
            }
            server.joinGame(new JoinGameRequest(authToken,teamColor, String.valueOf(listedGames.get(id-1))));
            System.out.println("Successfully joined game " + id);
            inGameClient.join(id,authToken,teamColor);
        } catch(Throwable e){
            System.out.print(SET_TEXT_COLOR_RED);

            if(e.getClass() == DataAccessException.class){
                System.out.println("Response from server: " + sliceOutMessage(e.getMessage()));
            } else {
                System.out.println("There was an error while joining the game");
            }
        }
    }

    private void listGames(){
        if(!validState(ClientStates.LOGGED_IN)){
            System.out.println(invalidCommand);
            return;
        }
        try {
            ListGamesResult result = server.listGames(new ListGamesRequest(authToken));
            listedGames.clear();
            int counter = 0;
            for(var game : result.games()){
                counter++;
                listedGames.add(game.gameID());
                StringBuilder builder = new StringBuilder();
                builder.append("Game #");
                builder.append(counter);
                builder.append(": ");
                builder.append(game.gameName());
                builder.append("\nWhite Player: ");
                builder.append((game.whiteUsername() == null) ? "" :  game.whiteUsername());
                builder.append(", Black Player: ");
                builder.append((game.blackUsername() == null) ? "" : game.blackUsername());
                System.out.println(builder);
            }
            if(counter == 0){
                System.out.println(SET_TEXT_COLOR_YELLOW + "There are no games to list");
            }
        } catch(Throwable e){
            System.out.print(SET_TEXT_COLOR_RED);

            if(e.getClass() == DataAccessException.class){
                System.out.println("Response from server: " + sliceOutMessage(e.getMessage()));
            } else {
                System.out.println("There was an error while listing the games");
            }
        }

    }

    private boolean validID(String string){
        int num;
        try {
            num = Integer.parseInt(string);
        } catch (Throwable e){
            System.out.println(SET_TEXT_COLOR_RED + "Please provide a valid game ID");
            return false;
        }
        return (num > 0 && num <= listedGames.size());
    }

    private void observeGame(int id){
        inGameClient.join(id,authToken,null);
    }

    public ClientStates getClientState(){
        return state;
    }

    private String sliceOutMessage(String message){
        message = message.substring(message.indexOf("Error:"));
        message = message.substring(7,(message.length())-2);
        return message;
    }

    public boolean getHasQuit(){
        return hasQuit;
    }

    public void setHasQuit(boolean newVal){
        hasQuit = newVal;
        if(validState(ClientStates.LOGGED_IN)) {logout();}
    }

    public boolean getBreakLoop(){
        return breakLoop;
    }

    public void setBreakLoop (boolean val){
        breakLoop = val;
    }
}
