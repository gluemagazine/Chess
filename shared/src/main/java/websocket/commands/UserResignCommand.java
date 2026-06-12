package websocket.commands;

public class UserResignCommand extends UserGameCommand{
    boolean observing = false;
    public UserResignCommand(CommandType commandType, String authToken, Integer gameID, boolean observer) {
        super(commandType, authToken, gameID);
        observing = observer;
    }

    public boolean getObserving(){
        return observing;
    }
}
