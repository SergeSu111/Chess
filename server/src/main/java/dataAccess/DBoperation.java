package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

// 这个class其实是多余的 这个其实就是Data Access 操作数据库的函数定义 只不过把所有的数据操作方法都写到一个class里然后调用罢了
// 创建一个能够操作数据库所有服务的东西 这样其他的服务直接call这个class就可以了
public class DBoperation {
    private static  HashSet<AuthData> authData = new HashSet<>();  // for Auth data
    private static  HashSet<GameData> gameData = new HashSet<>(); // for game data
    private static HashSet<UserData> userData = new HashSet<>(); // for user data

    private static int myNextId = 100;

    public void clearAuth() {
        authData.clear(); // 将auth data 清空 clear是HashSet自己的function
    }

    public void clearUser() {
        userData.clear(); // 将 user data 数据清空 clear是HashSet自己的function
    }

//    public void clearGame() {
//        gameData.clear(); // 将 game data 数据清空 clear 是HashSet自己的function
//    }

    public UserData getUser(String arguments, UserData search) throws IllegalAccessException {
        switch (arguments) {
            case "username" -> {
                for (UserData curr : userData) {
                    if (search.username().equals(curr.username())) // 如果要搜索的名字在数据库里证明不需要注册了
                    {
                        return curr; // 直接返回
                    }
                }
            }
            case "password" -> {
                for (UserData curr : userData) {
                    if (search.password().equals(curr.password())) // 如果搜索的密码也在数据库里
                    {
                        return curr; // 直接返回
                    }
                }
            }
            case "email" -> {
                for (UserData curr : userData) {
                    if (search.email().equals(curr.email())) // 如果搜索的email也在数据库里
                    {
                        return curr;
                    }
                }
            }
            case "all" -> {
                for (UserData curr : userData) {
                    if (search == curr) {
                        return curr;
                    }
                }
            }
            default -> throw new IllegalAccessException("Your arguments are not right");
        }
        return null;
    }

    public void createUser(String username, String password, String email) {
        userData.add(new UserData(username, password, email));
    }

    public AuthData getAuth(String argument, AuthData search) throws IllegalAccessException {
        switch (argument) {
            case "authToken" -> {
                for (AuthData curr : authData) {
                    if (search.authToken().equals(curr.authToken())) // 如果我当前的auth在数据库
                    {
                        return curr;
                    }
                }
            }
            case "username" -> {
                for (AuthData curr : authData) {
                    if (search.username().equals(curr.username())) // 如果当前的username在数据库
                    {
                        return curr;
                    }
                }
            }
            case "all" -> {
                for (AuthData curr : authData) {
                    if (search.equals(curr)) {
                        return curr;
                    }
                }
            }
            default -> throw new IllegalAccessException("invalid search argument");
        }
        return null;
    }

    public void createAuth(String authToken, String username) {
        authData.add(new AuthData(authToken, username));
    }

    public void deleteAuth(String argument, AuthData search) throws IllegalAccessException {
        AuthData removeOne = getAuth(argument, search);
        while (removeOne != null)
        {
            authData.remove(removeOne);
            removeOne = getAuth(argument, search);
        }

    }

//    public int getNewGameId() {
//        myNextId = myNextId + 1;
//        return myNextId;
//    }

//    public HashSet <GameData> showAllGames() {
//        return (gameData);
//    }

    public void createGame(int gameId, String whiteUsername, String blackUsername, String gameName, String game) {
        gameData.add(new GameData(gameId, whiteUsername, blackUsername, gameName, game));
    }

    public GameData getGame(String arguments, GameData search) throws IllegalAccessException {
        switch (arguments) {
            case "gameID" -> {
                for (GameData curr : gameData) {
                    if (Objects.equals(search.gameID(), curr.gameID())) {
                        return curr;
                    }
                }
            }
            case "whiteUsername" -> {
                for (GameData curr : gameData) {
                    if (search.whiteUsername().equals(curr.whiteUsername())) {
                        return curr;
                    }
                }
            }

            case "blackUsername" -> {
                for (GameData curr : gameData) {
                    if (search.blackUsername().equals(curr.blackUsername())) {
                        return curr;
                    }
                }
            }

            case "game" -> {
                for (GameData curr : gameData) {
                    if (search.game().equals(curr.game())) {
                        return curr;
                    }
                }
            }

            case "all" -> {
                for (GameData curr : gameData) {
                    if (search == curr) {
                        return curr;
                    }
                }
            }
            default -> throw new IllegalAccessException("invalid search filed");
        }
        return null;
    }

    public void delGame (String arguments, GameData search) throws IllegalAccessException
    {
        GameData removed = getGame(arguments, search);
        while (removed != null)
        {
            gameData.remove(removed);
            removed = getGame(arguments, search);
        }

    }
}
