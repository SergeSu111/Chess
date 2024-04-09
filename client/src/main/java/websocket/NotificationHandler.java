package websocket;

import WebSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage serverMessage);
}
