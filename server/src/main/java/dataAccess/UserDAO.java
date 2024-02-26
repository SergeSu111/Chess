package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public void createUser(String username, String email, String password) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
}
