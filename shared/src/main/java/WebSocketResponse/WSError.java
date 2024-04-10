package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;

public class WSError extends ServerMessage {

    private final String errorMessage;
    public WSError(String errorMessage, ServerMessageType type) {
        super(type);

        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }
}