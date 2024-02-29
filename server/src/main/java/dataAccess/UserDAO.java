package dataAccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(String username, String email, String password) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException, IllegalAccessException;

    boolean userIsStored(String username) throws DataAccessException, IllegalAccessException;

    boolean passwordMatch(String testUsername, String password) throws DataAccessException, IllegalAccessException;
}
