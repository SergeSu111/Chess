package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;

public class Error extends ServerMessage {
    public Error(ServerMessageType type) {
        super(type);
    }

    private String errorMessage;
}
