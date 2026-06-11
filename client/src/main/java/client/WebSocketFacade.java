package client;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{

    private Session session;
    public WebSocketFacade(String url,ServerMessageObserver observer) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage placeholder = new Gson().fromJson(message,ServerMessage.class);
                    ServerMessage actual = switch (placeholder.getServerMessageType()){
                        case LOAD_GAME -> new Gson().fromJson(message,LoadGameMessage.class);
                        case ERROR -> new Gson().fromJson(message,ErrorMessage.class);
                        case NOTIFICATION -> new Gson().fromJson(message,NotificationMessage.class);
                    };
                    observer.notify(actual);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void sendCommand(UserGameCommand command) {
        this.session.getAsyncRemote().sendText(new Gson().toJson(command));
    }
}
