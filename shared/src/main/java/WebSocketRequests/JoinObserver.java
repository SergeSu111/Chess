package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand
{

    public JoinObserver(String authToken) {
        super(authToken);
    }

    private Integer gameID;
}
