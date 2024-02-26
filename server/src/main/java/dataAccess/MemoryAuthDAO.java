package dataAccess;

import model.AuthData;

import java.util.HashSet;
// 其实memory的class就是指代的sql.
public class MemoryAuthDAO implements AuthDAO
{

    private static final HashSet<AuthData> authSet = new HashSet<AuthData>(); // 存储AuthData
    @Override
    public void clear() throws DataAccessException
    {
        authSet.clear();
    }

    @Override
    public void createAuth() throws DataAccessException
    {
       
    }

    @Override
    public AuthData getAuth() throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth() throws DataAccessException {

    }

}


