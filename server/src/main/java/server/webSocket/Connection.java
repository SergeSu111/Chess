package server.webSocket;
import WebSocketMessages.serverMessages.ServerMessage;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

// 代表买一个加入webscoket的用户
public class Connection {
    public String memberAuthToken;
    public Session session;
    // 表示webscoket的连接会话 提供管理连接的方法和属性. 可以发消息给客户端 获取连接的id 和远程端点信息

    public Connection(String memberUsername, Session session)
    {
        this.memberAuthToken = memberUsername;
        this.session = session;
    }

    // why we still need send message in connection? In WHandler we just send message to all client.
    public void send(String msg) throws IOException
    {
        session.getRemote().sendString(msg);
        // getRemote()就是拥有权限把消息发送给一个其他人
    }


}
