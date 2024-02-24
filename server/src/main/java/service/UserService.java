package service;

import dataAccess.*;
import request.RegisterRequest;

public class UserService {  // 服务有Auth 相关的数据和接口功能   Game相关的数据和接口功能  还有user相关的数据和接口功能

    // 将每个接口和其存储的数据和方法都拿过来
    private final AuthDAO authDAO = new MemoryAuthDAO();

    //    private final GameDAO gameDAO = new MemoryGameDAO();
//
//    private final UserDAO userDAO = new MemoryUserDAO();
    public void clear() {
        try {
            authDAO.clear(); // service里的功能定义 如果清理异常
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RegisterRequest register(RegisterRequest r) {
         String authToken;

    }
}
