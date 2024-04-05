package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;

public class Leave extends UserGameCommand {
    public Leave(String authToken) {
        super(authToken);
    }

    private Integer gameID;

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
