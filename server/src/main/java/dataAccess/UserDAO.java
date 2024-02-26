package dataAccess;

import model.UserData;

public interface UserDAO {
    boolean clear();
    boolean create_user(String username, String email, String password) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
