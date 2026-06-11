package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private final String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
    public ErrorMessage(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getMessage(){
        return errorMessage;
    }
}
