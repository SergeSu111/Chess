package WebSocketResponse;

import WebSocketMessages.serverMessages.ServerMessage;
import chess.ChessGame;

public class LoadGame extends ServerMessage {
    public LoadGame(ServerMessageType type) {
        super(type);
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    private ChessGame game; // I am not sure
}
