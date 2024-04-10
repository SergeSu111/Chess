package server.webSocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import WebSocketResponse.LoadGame;
import WebSocketResponse.Notification;
import WebSocketResponse.Error;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;


//代表webscoket里处理用户加入 删除 的class
public class ConnectionManager {
    // 可以并发的hashmap. Concurrent 并发 指的是多个事件在同一时间发生 比如同时有好几个客户都加入游戏或观察游戏 或退出
    //

    // 大的dictionary 是整个游戏 小的hashmap是一个小游戏
    public ConcurrentHashMap<Integer, Vector<Connection>> connections = new ConcurrentHashMap<>();

    // add member to party
    // 每一个member都需要自己独特的session 可以和别人对话 进行连接 所以需要session
    public void add(Integer gameID, String memberAuthToken, Session session) {
        // 根据用户username 和session注册一个新的成员
        var connection = new Connection(memberAuthToken, session);
        Vector<Connection> singleGame = connections.get(gameID); // 得到用户在的gameID的游戏位置
        if (singleGame == null) // 如果游戏为空的
        {
            singleGame = new Vector<>(); // 则创建一个空的游戏
        }
        singleGame.add(connection); // 把connection加进去
        connections.put(gameID, singleGame);
    }

    // remove member from party
    public void remove(String memberAuthToken,Integer gameID)
    {
        // 根据key 来删除这个connection
        Vector<Connection> singleGame = this.connections.get(gameID); // 得到了要删除的游戏局
        for (Connection c : singleGame)
        {
            if (Objects.equals(c.memberAuthToken, memberAuthToken)) // 如果找到了我要删除的人
            {
                singleGame.remove(c); // 从game里删除
            }
        }
    }

    // broadcast
    // 如果玩家移动棋子 websocket是不用提醒这个玩家的 而要提醒其他玩家 所以需要excludeVistorName
    public void broadcast(String excludeVistorName, Notification serverMessage, Integer gameID, Boolean includedSelf) throws IOException
    {
        // 对于一些没有办法分享信息的成员 就放进来 之后踢掉

        var removeList = new ArrayList<Connection>();
        // 得到connections里gameID这个游戏
        Vector<Connection> singleGame = this.connections.get(gameID); // 根据游戏ID来得到这个游戏局
        for (Connection connection : singleGame)
        {
            if (connection.session.isOpen())  // 如果这个成员没有睡着
            {
                if (!connection.memberAuthToken.equals(excludeVistorName) || includedSelf) // 如果这个成员是我们要发送给的
                {
                    //String msg = new Gson().toJson(serverMessage, ServerMessage.class);
                    String msg = new Gson().toJson(serverMessage, Notification.class);
                    connection.send(msg); // 那么就发送消息给这个connection
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

    public void loadMessage(int gameID, String authToken, LoadGame loadGame) throws IOException {
        var removeList = new Vector<Connection>();
        for (var c : connections.get(gameID))
        {
            if (c.session.isOpen())
            {
                if (c.memberAuthToken.equals(authToken))
                {
                    String msg = new Gson().toJson(loadGame, LoadGame.class);
                    c.send(msg);
                }
            }
            else
            {
                removeList.add(c);
            }
        }
        for(var connection : removeList){
            Vector<Connection>  newConnection = connections.get(gameID);
            newConnection.remove(connection);
            connections.put(gameID, newConnection);
        }
    }

    public void loadMessage(int gameID, LoadGame loadGame) throws IOException {
        var removeList = new Vector<Connection>();
        for (var c : connections.get(gameID))
        {
            if (c.session.isOpen())
            {
                String msg = new Gson().toJson(loadGame, LoadGame.class);
                c.send(msg);
            }
            else
            {
                removeList.add(c);
            }
        }
        for(var connection : removeList){
            Vector<Connection>  newConnection = connections.get(gameID);
            newConnection.remove(connection);
            connections.put(gameID, newConnection);
        }
    }



    // 传入一个authToken 和error message 来对这个authToken的用户发送错误消息
    public void sendError(String authToken, Error error) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<Connection>();
        for (int gameID : connections.keySet()) {
            for (var c : connections.get(gameID)) // 当前game的每一个用户
            {
                if (c.session.isOpen()) {
                    if (c.memberAuthToken.equals(authToken)) // 如果这个用户是我要找的用户
                    {
                        String message = new Gson().toJson(error); // 就会把这个error发送给用户
                        c.send(message);
                    }
                } else {
                    removeList.add(c); // 如果睡着了就放入removeList里
                }
            }

            for (var c : removeList) {
                Vector<Connection> tmp = connections.get(gameID);
                tmp.remove(c);
                connections.put(gameID, tmp);
            }
        }


    }
    }


