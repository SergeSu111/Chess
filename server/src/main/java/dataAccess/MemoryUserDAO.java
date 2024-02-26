package dataAccess;

import model.UserData;
import org.w3c.dom.UserDataHandler;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO
{
    private static final HashSet<UserData> userSet = new HashSet<UserData>();

    public void clear()
    {
        userSet.clear();
    }

    @Override
    public void createUser(String username, String email, String password) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
