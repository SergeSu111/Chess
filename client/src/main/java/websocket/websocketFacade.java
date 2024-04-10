//package websocket;
//import WebSocketRequests.*;
//import WebSocketResponse.Notification;
//import chess.ChessGame;
//import chess.ChessMove;
//import com.google.gson.Gson;
//import com.sun.nio.sctp.NotificationHandler;
//import model.AuthData;
//import ui.ResponseException;
//
//import javax.websocket.*;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.net.http.WebSocket;
//
//public class websocketFacade extends Endpoint
//{
//    Session session;
//    NotificationHandler notificationHandler;
//
//
//    public websocketFacade(String url, websocket.NotificationHandler notificationHandler) throws ResponseException
//    {
//        try
//        {
//            url = url.replace("http", "ws");
//            URI socketURI = new URI(url + "/connect");
//
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            this.session = container.connectToServer(this, socketURI);
//
//            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
//                @Override
//                public void onMessage(String message)
//                {
//                    Notification notification = new Gson().fromJson(message, Notification.class);
//                    notificationHandler.notify(notification);
//                }
//
//            });
//        } catch (DeploymentException | URISyntaxException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig)
//    {}
//
//    public void joinPlayer(AuthData auth, int gameID, ChessGame.TeamColor teamColor) throws ResponseException
//    {
//        try
//        {
//            JoinPlayer joinPlayerCMD = new JoinPlayer(auth.authToken(), gameID, teamColor);
//            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayerCMD));
//        }
//        catch (IOException ex)
//        {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    public void joinObserver(AuthData auth, int gameID) throws ResponseException, IOException {
//        try
//        {
//            JoinObserver joinObserverCMD = new JoinObserver(auth.authToken(), gameID);
//            this.session.getBasicRemote().sendText(new Gson().toJson(joinObserverCMD));
//        }
//        catch(IOException ex)
//        {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    public void leave (AuthData auth, int gameID) throws ResponseException, IOException {
//        try
//        {
//            Leave leaveCMD = new Leave(auth.authToken(), gameID);
//            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCMD));
//        }
//        catch(IOException ex)
//        {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    public void resign(AuthData auth, int gameID) throws ResponseException, IOException
//    {
//        try
//        {
//            Resign ResignCMD = new Resign(auth.authToken(), gameID);
//            this.session.getBasicRemote().sendText(new Gson().toJson(ResignCMD));
//        }
//        catch(IOException e)
//        {
//            throw new ResponseException(500, e.getMessage());
//        }
//    }
//
//    public void makeMove(AuthData auth, int gameID, ChessMove move) throws ResponseException
//    {
//        try
//        {
//            MakeMove makeMoveCMD = new MakeMove(auth.authToken(), gameID, move);
//            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCMD));
//        }
//        catch (IOException E)
//        {
//            throw new ResponseException(500, E.getMessage());
//        }
//    }
//
//
//

//
//
//}
