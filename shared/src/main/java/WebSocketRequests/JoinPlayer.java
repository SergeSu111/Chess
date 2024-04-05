package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;
import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    public JoinPlayer(String authToken) {
        super(authToken);
    }

    private Integer gameID;
    private ChessGame.TeamColor playColor;
}
