package service;

import dataAccess.*;
import model.AuthData;
import request.RegisterRequest;

import java.util.Collection;

public class UserService {  // 服务有Auth 相关的数据和接口功能   Game相关的数据和接口功能  还有user相关的数据和接口功能

    // 将每个接口和其存储的数据和方法都拿过来
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = (GameDAO) new MemoryGameDAO();
    private final UserDAO userDAO = (UserDAO) new MemoryUserDAO();

    public void clear() {
        try {
            authDAO.clear(); // service里的功能定义 如果清理异常
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            userDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public AuthData Register(String username, String email, String password) {
        try {
            userDAO.createUser(username, email, password);
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // why its runtime exception?
        }
        return null; // why return null?
    }

    public Collection<AuthData> listAuth() // WHY THIS FUNCTION?
    {
        return (Collection<AuthData>) authDAO;
    }

}
