package WebSocketRequests;

import WebSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {

    public Resign(String authToken) {
        super(authToken);
    }

    private Integer gameID;
}
