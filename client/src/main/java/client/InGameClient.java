package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.BoardPrinter;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class InGameClient implements ServerMessageObserver{

    String invalidCommand = SET_TEXT_COLOR_RED + "Invalid command or arguments, type \"help\" to get a list of valid commands and parameters.";

    private String normalHelp =
            """ 
            redraw - the current chess board
            leave - the game
            move <Start position> <End Position> - Moves from start to end. Positions are <A-H><1-8>
            resign - resigns the game
            highlight <Position> - shows all legal moves at the given position. Positions are <A-H><1-8>
            help - display all current valid commands""";

    private String observingHelp =
            """ 
            redraw - the current chess board
            leave - the game
            highlight <Position> - shows all legal moves at the given position. Positions are <A-H><1-8>
            help - display all current valid commands""";



    private final BoardPrinter printer = new BoardPrinter();
    private final WebSocketFacade socket;
    private boolean observing;
    private boolean hasLeft = false;
    private ChessGame game;
    private ChessGame.TeamColor color;

    public InGameClient(WebSocketFacade socket){
        this.socket = socket;

    }

    @Override
    public void notify(ServerMessage message) {
        System.out.print(message);
        System.out.print("\n\n[IN_GAME] >>>");
    }

    public void processInput(String input){
        String[] params = input.split(" ");
        try {
            switch (params[0].toLowerCase()){
                case "leave" :
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for leave");
                        break;
                    }
                    leave();
                    break;
                case "help":
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for help");
                        break;
                    }
                    break;
                case "resign":
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for resign");
                        break;
                    }
                    resign();
                    break;
                case "redraw":
                    if ((params.length != 1)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "No parameters needed for redraw");
                        break;
                    }
                    redraw();
                    break;
                default:
                    System.out.println(invalidCommand);
            }
        } catch (Throwable e){
            System.out.println(invalidCommand);
        }
    }

    private void printGame(ChessGame game, ChessGame.TeamColor color){
        printer.printChessBoard(game,color);
    }

    public void join(int gameID, String authToken, ChessGame.TeamColor color){
        String helpText = (color == null) ? observingHelp : normalHelp;
        this.color = (color != null) ? color : ChessGame.TeamColor.WHITE;
        new InGameRepl(helpText,this);
    }

    private void connect(int gameID, String authToken){
        socket.connect(gameID,authToken);
    }

    private void resign(){

    }

    private void redraw(){
        printer.printChessBoard(game,color);
    }

    private void highlightSquare(){

    }

    private void makeMove(){

    }

    private void leave(){

        hasLeft = true;
    }

    public boolean getHasLeft(){
        return hasLeft;
    }
}
