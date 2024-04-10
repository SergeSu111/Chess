package server.webSocket;

import WebSocketMessages.serverMessages.ServerMessage;
import WebSocketMessages.userCommands.UserGameCommand;
import WebSocketRequests.*;
import WebSocketResponse.*;
import WebSocketResponse.WSError;
import WebSocketResponse.Notification;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.sqlAuth;
import dataAccess.sqlGame;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

// 很像webscoket服务器 接收来自webscoket成员的信息然后返回传播给其他webscket成员的class
@WebSocket
public class WebScoketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();


    // onmessage 一定需要一个session 和S挺message message可以是"JoinPlayer"
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, IllegalAccessException {
        // 将message转换为userGameCommand 但随之也会自己joinPlayer class的属性也会消失
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch(userGameCommand.getCommandType())
        {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class));
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class));
            case RESIGN -> reSign(new Gson().fromJson(message, Resign.class));
        }
    }


    private void joinPlayer(JoinPlayer joinplayer, Session session) throws IOException, DataAccessException, IllegalAccessException {
        isGameIDAndAuthValid(joinplayer, session);
        ChessGame.TeamColor teamColor = joinplayer.getPlayerColor(); // 得到用户加入游戏的颜色是什么

        // 检查用户加入游戏的颜色有没有被占用
        sqlGame mysqlGame = new sqlGame();
        sqlAuth mysqlAuth = new sqlAuth(); // 得到sqlAuth来get username
        GameData game = mysqlGame.getGame(joinplayer.getGameID());
        ChessGame chessgame = new Gson().fromJson(game.game(), ChessGame.class);
        String username = mysqlAuth.getUserName(joinplayer.getAuthString()); // 得到了username
        if (teamColor == ChessGame.TeamColor.BLACK)
        {
            if (!username.equals(game.blackUsername()))
            {
                try
                {
                    connectionManager.sendError(joinplayer.getAuthString(),
                            new WSError("Error: This username is already taken.") );
                    System.out.println("sent error");
                }
                catch(IOException E)
                {
                    throw new IOException(E.getMessage());
                }
                return;
            }
        }

        if (teamColor == ChessGame.TeamColor.WHITE)
        {
            if(!username.equals(game.whiteUsername()))
            {
                try
                {
                    connectionManager.sendError(joinplayer.getAuthString(), new WSError("Error: This username is already taken."));
                    System.out.println("send error");
                }
                catch (IOException E)
                {
                    throw new IOException(E.getMessage());
                }
                return;
            }
        }


        // 通过sqlgame 得到username
         // 创造一个myConnectionManager
        Notification notification = new Notification("A player " + username + " is joining the game with team: " + teamColor); // 会得到一个joinPlayer 的notification
        var loadGame = new LoadGame(chessgame);
        try
        {
            connectionManager.broadcast(joinplayer.getAuthString(), notification, joinplayer.getGameID()); // 然后我再把这个notification发送给其他所有人
            connectionManager.loadMessage(joinplayer.getGameID(), joinplayer.getAuthString(), loadGame);
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }

        // get the game by ID FROM DB



        // session.getremote().send(loadgame)
        //session.getRemote().sendString(new Gson().toJson(loadGame.getGame()));
    }

    private void joinObserver(JoinObserver joinObserver, Session session) throws DataAccessException, IllegalAccessException, IOException {

        isGameIDAndAuthValid(joinObserver, session);
        int gameID = joinObserver.getGameID();
        String auth = joinObserver.getAuthString();

        sqlGame mysqlGame = new sqlGame();
        sqlAuth mysqlAuth = new sqlAuth(); // 得到sqlAuth来get username

        GameData game = mysqlGame.getGame(gameID);
        ChessGame chessGame = new Gson().fromJson(game.game(), ChessGame.class);
        String username = mysqlAuth.getUserName(auth); // 得到username

        Notification notification = new Notification("A player called " + username + " is observing the game.");
        var loadGame = new LoadGame(chessGame);
        try
        {
            connectionManager.broadcast(auth, notification, gameID);
            connectionManager.loadMessage(gameID, auth, loadGame);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void makeMove(MakeMove move) throws DataAccessException, IllegalAccessException, IOException {
        int gameID = move.getGameID();
        String auth = move.getAuthString();
        sqlGame theSqlGame = new sqlGame();
        sqlAuth theSqlAuth = new sqlAuth();
        String username = theSqlAuth.getUserName(auth); // 得到了username
        GameData theGame = theSqlGame.getGame(gameID); // 得到了gameData
        ChessMove theMove = move.getMove(); // 得到了当前可以走的所有的路线
        String StrGame = theGame.game(); // 根据GameData得到了StrGame
        ChessGame realGame = new Gson().fromJson(StrGame, ChessGame.class); // 将strGame转变为realGame
        ChessPiece startPiece = realGame.getBoard().getPiece(theMove.getStartPosition()); // 得到了这个棋子的起始位置


        ChessGame.TeamColor selfColor;
        if(theGame.whiteUsername().equals(username)){
            selfColor = ChessGame.TeamColor.WHITE;
        } else if (theGame.blackUsername().equals(username)) {
            selfColor = ChessGame.TeamColor.BLACK;
        }
        else {
            connectionManager.sendError(auth, new WSError("not your turn"));
            return;
        }

        if(selfColor != realGame.getBoard().getPiece(theMove.getStartPosition()).getTeamColor()){
            connectionManager.sendError(auth, new WSError("not your turn"));
            return;
        }

        // try to make move
        try
        {
            realGame.makeMove(theMove);
        }
        catch (Exception e)
        {
            try {
                connectionManager.sendError(auth, new WSError(e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }

        // put the updated realGame into db
        theSqlGame.updateGame(realGame, gameID);
        var loadGameMessage = new LoadGame(realGame);  // put into loadGame
        Notification notification = new Notification(username + " is moving " + startPiece.getPieceType().toString() + " from " +
                theMove.getStartPosition().toString() + " to " + theMove.getEndPosition().toString() + ".");
        try
        {
            connectionManager.broadcast(auth, notification, gameID);  // 将notification传给其他所有用户
            connectionManager.loadMessage(gameID, loadGameMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void leave(Leave leave)
    {}

    private void reSign(Resign resign)
    {}

    private void isGameIDAndAuthValid(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException, IllegalAccessException {
        int gameID = userGameCommand.getGameID();
        String auth = userGameCommand.getAuthString();
        connectionManager.add(gameID, auth, session); // 将这个用户加入到big party里去

        sqlGame mysqlGame = new sqlGame();
        sqlAuth mysqlAuth = new sqlAuth(); // 得到sqlAuth来get username

        if(!mysqlGame.gameExists(gameID)) // false
        {
            try
            {
                connectionManager.sendError(userGameCommand.getAuthString(), new WSError("Error: The game ID is not exist."));
                System.out.println("send error");
            }
            catch( IOException e)
            {
                throw new IOException(e.getMessage());
            }
            return;
        }

        if (!mysqlAuth.authIsStored(auth))
        {
            try
            {
                connectionManager.sendError(userGameCommand.getAuthString(), new WSError("Error: The user is not exist."));
                System.out.println("send error");
            }
            catch( IOException e)
            {
                throw new IOException(e.getMessage());
            }
            return;
        }


    }


}
