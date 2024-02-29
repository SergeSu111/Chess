package dataAccess;

import model.AuthData;

import java.util.UUID;

public class MemoryAuth implements AuthDAO{

    private final DBoperation dBoperation = new DBoperation();
    @Override
    public void clear() throws DataAccessException {
        dBoperation.clearAuth();
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String newAuthToken = UUID.randomUUID().toString(); // randomly create a UUID special one
        dBoperation.createAuth(newAuthToken, username);
        return newAuthToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException, IllegalAccessException {
        return dBoperation.getAuth("authToken", new AuthData(authToken, null)); // 因为只用authToken搜索. 所以username 是null
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, IllegalAccessException {
        dBoperation.deleteAuth("authToken", new AuthData(authToken, null));
    }

    /*调用getAuth来看这个authToken是不是在数据库里 在返回true 不在返回false*/
    @Override
    public boolean authIsStored(String authToken) throws DataAccessException, IllegalAccessException {
        return getAuth(authToken) != null;
    }

    public String getUserName(String authToken) throws IllegalAccessException {
        try
        {
            AuthData myAuth = getAuth(authToken);
            return myAuth.username();
        }
        catch (Exception e)
        {
            throw new IllegalAccessException("Error: unauthorized");
        }

    }

}
