package websocket;
import WebSocketMessages.serverMessages.ServerMessage;
import WebSocketRequests.*;
import WebSocketResponse.LoadGame;
import WebSocketResponse.Notification;
import WebSocketResponse.WSError;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import ui.BOARD;
import ui.GamePlay;
import ui.ResponseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint
{
    Session session;
    Notification notification;
    WSError error;
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
                    if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION))
                    {
                        notification = new Gson().fromJson(message, Notification.class); // 转变为notification class
                    }
                    else if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR))
                    {
                        error = new Gson().fromJson(message,WSError.class);
                    }
                    switch (serverMessage.getServerMessageType())
                    {
                        case NOTIFICATION ->

                                System.out.println(notification.getMessage());
                        case ERROR ->
                                System.out.println(error.getErrorMessage());

                        case LOAD_GAME -> {
                            LoadGame gameObj = new Gson().fromJson(message, LoadGame.class);
                            ChessGame currentGame = gameObj.getGame();
                            ChessGame game = gameObj.getGame();
                            BOARD board = new BOARD();
                            BOARD.drawGeneralBoard(game.getBoard(), GamePlay.playerColor);

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

    public void leave (String auth, int gameID) throws ResponseException, IOException {
        try
        {
            Leave leaveCMD = new Leave(auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCMD));
        }
        catch(IOException ex)
        {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resign(String auth, int gameID) throws ResponseException, IOException
    {
        try
        {
            Resign resignCMD = new Resign(auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(resignCMD));
        }
        catch(IOException e)
        {
            throw new ResponseException(e.getMessage());
        }
    }

    public void makeMove(String auth, int gameID, ChessMove move) throws ResponseException
    {
        try
        {
            MakeMove makeMoveCMD = new MakeMove(auth, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCMD));
        }
        catch (IOException E)
        {
            throw new ResponseException(E.getMessage());
        }
    }






}
