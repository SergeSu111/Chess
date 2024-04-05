package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;
import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    public MakeMove(String authToken) {
        super(authToken);
    }

    private Integer gameID;
    private ChessMove move;
}
