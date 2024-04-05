package server.webSocket;


import WebSocketMessages.userCommands.UserGameCommand;
import WebSocketRequests.*;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

// 很像webscoket服务器 接收来自webscoket成员的信息然后返回传播给其他webscket成员的class
public class WebScoketHandler {
    private final ConnectionManager connections = new ConnectionManager();


    // onmessage 一定需要一个session 和S挺message message可以是"JoinPlayer"
    public void onMessage(Session session, String message) throws IOException
    {
        // 将message转换为userGameCommand 但随之也会自己joinPlayer class的属性也会消失
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch(userGameCommand.getCommandType())
        {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class));
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class));
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class));
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class));
            case RESIGN -> reSign(new Gson().fromJson(message, Resign.class));
        }
    }

    private void joinPlayer(JoinPlayer joinplayer)
    {

    }

    private void joinObserver(JoinObserver joinObserver)
    {
    }

    private void makeMove(MakeMove move)
    {}

    private void leave(Leave leave)
    {}

    private void reSign(Resign resign)
    {}


}
