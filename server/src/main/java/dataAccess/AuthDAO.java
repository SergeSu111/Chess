package dataAccess;

import model.AuthData;

public interface AuthDAO {
    boolean clear() throws DataAccessException;  // 万一没有东西clear 就throw exception

    String createAuth(String username) throws DataAccessException; // authToken应该是个String

    AuthData getAuth(String authToken) throws DataAccessException;

    boolean  deleteAuth(String authToken) throws DataAccessException;
}
