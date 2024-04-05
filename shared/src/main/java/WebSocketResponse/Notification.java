package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;

public class Notification extends ServerMessage {
    public Notification(ServerMessageType type) {
        super(type);
    }

    private String message;


}
