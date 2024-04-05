package server.webSocket;


import WebSocketMessages.userCommands.UserGameCommand;
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
            case JOIN_PLAYER -> joinPlayer();
            case JOIN_OBSERVER -> joinObserver();
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> reSign();
        }
    }

    private void joinPlayer()
    {}



}
