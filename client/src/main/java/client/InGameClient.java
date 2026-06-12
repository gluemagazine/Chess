package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.BoardPrinter;
import websocket.commands.UserGameCommand;
import websocket.commands.UserMakeMoveCommand;
import websocket.commands.UserResignCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class InGameClient implements ServerMessageObserver{

    String invalidCommand = SET_TEXT_COLOR_RED + "Invalid command or arguments, type \"help\" to get a list of valid commands and parameters.";

    private final String normalHelp =
            """ 
            redraw - the current chess board
            leave - the game
            move <Start position> <End Position> - Moves from start to end. Positions are <A-H><1-8>
            resign - resigns the game
            highlight <Position> - shows all legal moves at the given position. Positions are <A-H><1-8>
            help - display all current valid commands""";

    private final String observingHelp =
            """ 
            redraw - the current chess board
            leave - the game
            highlight <Position> - shows all legal moves at the given position. Positions are <A-H><1-8>
            help - display all current valid commands""";



    private final BoardPrinter printer = new BoardPrinter();
    private WebSocketFacade socket;
    private boolean hasLeft = false;
    private ChessGame game;
    private ChessGame.TeamColor color;
    private int gameID;
    private String authToken;

    public InGameClient(){
    }

    @Override
    public void notify(ServerMessage message) {
        if(message.getClass() == LoadGameMessage.class){
            game = ((LoadGameMessage) message).getGame();
            System.out.println();
            redraw();
        }
        else if(message.getClass() == NotificationMessage.class){
            System.out.print(((NotificationMessage) message).getMessage());
        }
        else if(message.getClass() == ErrorMessage.class){
            System.out.print(SET_TEXT_COLOR_RED + ((ErrorMessage) message).getMessage());
        }
        System.out.print(RESET_BG_COLOR + RESET_TEXT_COLOR + "\n[IN_GAME] >>> ");
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
                case "move":
                    if ((params.length != 3)) {
                        System.out.println(SET_TEXT_COLOR_RED + "Too many or not enough parameters for move");
                        break;
                    }
                    makeMove(new ChessPosition(params[1]), new ChessPosition(params[2]));
                    break;
                case "highlight":
                    if ((params.length != 2)) {
                        System.out.println(SET_TEXT_COLOR_RED + "Too many or not enough parameters for move");
                        break;
                    }
                    highlightSquare(new ChessPosition(params[1]));
                    break;
                default:
                    System.out.println(invalidCommand);
            }
        } catch (Throwable e){
            System.out.println(invalidCommand);
        }
    }

    public void join(int gameID, String authToken, ChessGame.TeamColor color){
        String helpText = (color == null) ? observingHelp : normalHelp;
        this.color = (color != null) ? color : ChessGame.TeamColor.WHITE;
        this.gameID = gameID;
        this.authToken = authToken;
        hasLeft = false;
        connect(gameID,authToken);
        new InGameRepl(helpText,this);
    }

    private void connect(int gameID, String authToken){
        UserGameCommand request = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID);
        socket.sendCommand(request);
    }

    private void resign(){
        System.out.println("Are you SURE you want to resign? (Y/N)");
        Scanner in = new Scanner(System.in);
        String response = in.nextLine();
        if(!response.strip().equalsIgnoreCase("Y")){
            return;
        }
        UserGameCommand request = new UserResignCommand(UserGameCommand.CommandType.RESIGN,authToken,gameID,(color == null));
        socket.sendCommand(request);
    }

    private void redraw(){
        printer.printChessBoard(game,color);
        System.out.println();
    }

    private void highlightSquare(ChessPosition pos){
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) game.validMoves(pos);
        ArrayList<ChessPosition> toHighlight = new ArrayList<>();
        toHighlight.add(pos);
        for (var move : validMoves){
            toHighlight.add(move.getEndPosition());
        }
        printer.printHighlighted(game,color,toHighlight);
    }

    private void makeMove(ChessPosition start, ChessPosition end){
        ChessMove move;
        ChessPiece piece = game.getBoard().getPiece(start);
        if(piece.getTeamColor() != color){
            System.out.println(SET_TEXT_COLOR_RED + "Error: You cannot move another player's piece");
            return;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            move = new ChessMove(start,end,getPromotion(start,end,color));
        }
        else {
            move = new ChessMove(start,end,null);
        }
        UserMakeMoveCommand makeMove = new UserMakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,authToken,gameID,move,color);
        socket.sendCommand(makeMove);
    }

    private ChessPiece.PieceType getPromotion(ChessPosition start,ChessPosition end, ChessGame.TeamColor color){
        int targetRow = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;
        if(end.getRow() != targetRow){
            return null;
        }
        ChessPiece.PieceType result = null;
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) game.validMoves(start);
        ChessMove testMove = new ChessMove(start,end, ChessPiece.PieceType.QUEEN);
        if(!validMoves.contains(testMove)){
            return null;
        }
        Scanner in = new Scanner(System.in);
        while(result == null){
            System.out.print("[IN_GAME]>>> What piece do you want? (B,Q,K,R)");
            result = switch(in.nextLine().strip().toUpperCase()){
                case "R" -> ChessPiece.PieceType.ROOK;
                case "Q" -> ChessPiece.PieceType.QUEEN;
                case "B" -> ChessPiece.PieceType.BISHOP;
                case "K" -> ChessPiece.PieceType.KNIGHT;
                default -> null;
            };
            if(result == null){
                System.out.println("Please enter a valid piece type");
            }
        }
        return result;
    }

    private void leave(){
        UserGameCommand request = new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken,gameID);
        socket.sendCommand(request);
        hasLeft = true;
    }

    public boolean getHasLeft(){
        return hasLeft;
    }
    public void setSocket(WebSocketFacade socket){
        this.socket = socket;
    }
}
