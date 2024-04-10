package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand
{

    public JoinObserver(String authToken, int gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.JOIN_OBSERVER;
    }
}
