package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;

public class WSError extends ServerMessage {

    private final String errorMessage;
    public WSError(String errorMessage) {
        super(ServerMessageType.ERROR);

        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }
}