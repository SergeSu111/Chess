package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;
import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    public JoinPlayer(String authToken) {
        super(authToken);
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    private Integer gameID;
    private ChessGame.TeamColor playColor;
}
