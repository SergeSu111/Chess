package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;
import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public MakeMove(String authToken) {
        super(authToken);
    }

    private Integer gameID;
    private ChessMove move;
}
