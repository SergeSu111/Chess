package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {
    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public Resign(String authToken) {
        super(authToken);
    }

    private Integer gameID;
}
