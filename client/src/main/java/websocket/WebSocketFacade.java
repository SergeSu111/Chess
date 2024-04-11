package websocket;
import WebSocketMessages.serverMessages.ServerMessage;
import WebSocketRequests.*;
import WebSocketResponse.LoadGame;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import ui.ResponseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint
{
    Session session;
    
    public WebSocketFacade(String url) throws ResponseException
    {
        try
        {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message)
                {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType())
                    {
                        case ERROR, NOTIFICATION -> System.out.println(serverMessage);
                        case LOAD_GAME -> {
                            LoadGame gameObj = new Gson().fromJson(message, LoadGame.class);
                            ChessGame currentGame = gameObj.getGame();
                            ChessGame game = gameObj.getGame();

                        }
                    }
                }

            });
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig)
    {}

    public void joinPlayer(String auth, int gameID, ChessGame.TeamColor teamColor) throws ResponseException
    {
        try
        {
            JoinPlayer joinPlayerCMD = new JoinPlayer(auth, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayerCMD));
        }
        catch (IOException ex)
        {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void joinObserver(String auth, int gameID) throws ResponseException, IOException {
        try
        {
            JoinObserver joinObserverCMD = new JoinObserver(auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinObserverCMD));
        }
        catch(IOException ex)
        {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leave (AuthData auth, int gameID) throws ResponseException, IOException {
        try
        {
            Leave leaveCMD = new Leave(auth.authToken(), gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCMD));
        }
        catch(IOException ex)
        {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resign(AuthData auth, int gameID) throws ResponseException, IOException
    {
        try
        {
            Resign resignCMD = new Resign(auth.authToken(), gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(resignCMD));
        }
        catch(IOException e)
        {
            throw new ResponseException(e.getMessage());
        }
    }

    public void makeMove(AuthData auth, int gameID, ChessMove move) throws ResponseException
    {
        try
        {
            MakeMove makeMoveCMD = new MakeMove(auth.authToken(), gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCMD));
        }
        catch (IOException E)
        {
            throw new ResponseException(E.getMessage());
        }
    }






}
