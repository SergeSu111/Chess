package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand
{
    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public JoinObserver(String authToken) {
        super(authToken);
    }

    private Integer gameID;
}