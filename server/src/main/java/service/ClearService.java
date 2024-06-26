package service;

import dataAccess.*;

import java.sql.SQLException;

public class ClearService {
    private GameDAO gameDAO = new SQLGame(); // 将接口和方法连接 这样只需要改变方法 不需要改变接口
    private UserDAO userDAO = new SQLUser(); // 将接口和方法链接 这样只需要改变方法 不需要改变接口

    private AuthDAO authDAO = new SQLAuth(); // 将接口和方法链接 这样只需要改变方法 不需要改变接口

    public ClearService() throws DataAccessException {
    }

    public void clear() throws DataAccessException, SQLException // handle 调用服务 服务再调用GameDao. GameDao 再调用自己的clear
    {
        gameDAO.clear();  // 删的是game hashset
        userDAO.clear();  // 山的是user hashset
        authDAO.clear(); // 删的是auth hashset
    }

}
