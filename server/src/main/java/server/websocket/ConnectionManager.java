package server.websocket;


import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, ArrayList<Session>> sessions = new ConcurrentHashMap<>();

    public void addToGame(int gameID, Session session){
        if(!sessions.containsKey(gameID)){
            sessions.put(gameID, new ArrayList<>());
        }
        sessions.get(gameID).add(session);
    }

    public void removeFromGame(int gameID, Session session){
        if(!sessions.containsKey(gameID)){
            return;
        }
        sessions.get(gameID).remove(session);
    }


    public void broadcast(int gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : sessions.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
