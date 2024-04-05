package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;

public class Notification extends ServerMessage {
    public Notification(ServerMessageType type) {
        super(type);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;


}
