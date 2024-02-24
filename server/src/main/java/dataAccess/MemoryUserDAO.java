package dataAccess;

import model.UserData;
import org.w3c.dom.UserDataHandler;

import java.util.HashSet;

public class MemoryUserDAO
{
    private static final HashSet<UserData> userSet = new HashSet<UserData>();

    public void clear()
    {
        userSet.clear();
    }
}
