package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;
import chess.ChessGame;

public class LoadGame extends ServerMessage {
    public LoadGame(ServerMessageType type) {
        super(type);
    }
    private ChessGame game; // I am not sure
}
