package server.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import WebSocketMessages.serverMessages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;


//代表webscoket里处理用户加入 删除 的class
public class ConnectionManager {
    // 可以并发的hashmap. Concurrent 并发 指的是多个事件在同一时间发生 比如同时有好几个客户都加入游戏或观察游戏 或退出
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    // add member to party
    // 每一个member都需要自己独特的session 可以和别人对话 进行连接 所以需要session
    public void add(String memberUsername, Session session)
    {
        // 根据用户username 和session注册一个新的成员
        var connection = new Connection(memberUsername, session);
        connections.put(memberUsername, connection); // 以键值对的形式 放进并发组里
    }

    // remove member from party
    public void remove(String memberUsername)
    {
        // 根据key 来删除这个connection
        connections.remove(memberUsername);
    }

    // broadcast
    // 如果玩家移动棋子 websocket是不用提醒这个玩家的 而要提醒其他玩家 所以需要excludeVistorName
    public void broadcast(String excludeVistorName, ServerMessage serverMessage) throws IOException
    {
        // 对于一些没有办法分享信息的成员 就放进来 之后踢掉
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values())
        {
            // 如果这个member没有睡着 可以被分享消息
            if (c.session.isOpen()) {
                if (!c.memberUsername.equals(excludeVistorName)) {
                    // 如果当前用户不是这个移动棋子的玩家
                    c.send(serverMessage.toString()); // 则把消息发送给这个用户
                }
            }
            else
            {
                // 否则不能被分享消息 就准备把它踢出party
                // 比如用户离开游戏了 就分享不到了
                removeList.add(c);
            }
        }
        // 将每一个离开的用户从party里删除
        for (var c : removeList)
        {
            connections.remove(c.memberUsername);
        }


    }
    }


