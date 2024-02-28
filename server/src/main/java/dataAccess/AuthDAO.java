package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;  // 万一没有东西clear 就throw exception

    String createAuth(String username) throws DataAccessException; // authToken应该是个String

    AuthData getAuth(String authToken) throws DataAccessException, IllegalAccessException;

    void  deleteAuth(String authToken) throws DataAccessException, IllegalAccessException;

    boolean auth_is_stored(String authToken) throws DataAccessException, IllegalAccessException;

    String get_user_name(String authToken) throws IllegalAccessException;
}
