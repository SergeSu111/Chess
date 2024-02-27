package dataAccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void create_user(String username, String email, String password) throws DataAccessException;
    UserData get_user(String username) throws DataAccessException, IllegalAccessException;

    boolean user_is_stored(String username) throws DataAccessException, IllegalAccessException;

    boolean password_match(String testUsername, String password) throws DataAccessException, IllegalAccessException;
}
