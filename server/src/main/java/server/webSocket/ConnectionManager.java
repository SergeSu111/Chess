package server.webSocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import WebSocketMessages.serverMessages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;


//代表webscoket里处理用户加入 删除 的class
public class ConnectionManager {
    // 可以并发的hashmap. Concurrent 并发 指的是多个事件在同一时间发生 比如同时有好几个客户都加入游戏或观察游戏 或退出
    //

    // 大的dictionary 是整个游戏 小的hashmap是一个小游戏
    public final ConcurrentHashMap<Integer, Vector<Connection>> connections = new ConcurrentHashMap<>();

    // add member to party
    // 每一个member都需要自己独特的session 可以和别人对话 进行连接 所以需要session
    public void add(Integer gameID, String memberAuthToken, Session session)
    {
        // 根据用户username 和session注册一个新的成员
        var connection = new Connection(memberAuthToken, session);
        Vector<Connection> singleGame = connections.get(gameID); // 得到用户在的gameID的游戏位置
        if (singleGame.equals(null)) // 如果游戏为空的
        {
            singleGame = new Vector<>(); // 则创建一个空的游戏
        }
        singleGame.add(connection); // 把connection加进去
    }

    // remove member from party
    public void remove(String memberAuthToken,Integer gameID)
    {
        // 根据key 来删除这个connection
        connections.remove(memberAuthToken);
    }

    // broadcast
    // 如果玩家移动棋子 websocket是不用提醒这个玩家的 而要提醒其他玩家 所以需要excludeVistorName
    public void broadcast(String excludeVistorName, ServerMessage serverMessage, Integer gameID, Session session) throws IOException
    {
        // 对于一些没有办法分享信息的成员 就放进来 之后踢掉

        var removeList = new ArrayList<Connection>();
        // 得到connections里gameID这个游戏
        Vector<Connection> singleGame = connections.get(gameID); // 根据游戏ID来得到这个游戏局
        for (Connection connection : singleGame)
        {
            if (connection.session.isOpen())  // 如果这个成员没有睡着
            {
                if (!connection.memberUsername.equals(excludeVistorName)) // 如果这个成员是我们要发送给的
                {
                    connection.send(serverMessage); // 那么就发送消息给这个connection
                }
            }
            else
            {
                // 否则不能被分享消息 就准备把它踢出party
                // 比如用户离开游戏了 就分享不到了
                removeList.add(connection);
            }
        }

        // 将每一个离开的用户从party里删除
        for (var c : removeList)
        {
            singleGame.remove(c);
        }


    }
    }


