package dataAccess;

import model.AuthData;

import java.util.UUID;

public class MemoryAuth implements AuthDAO{

    DBoperation dBoperation = new DBoperation();
    @Override
    public void clear() throws DataAccessException {
        dBoperation.clear_auth();
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String new_auth_token = UUID.randomUUID().toString(); // randomly create a UUID special one
        dBoperation.create_auth(new_auth_token, username);
        return new_auth_token;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException, IllegalAccessException {
        return dBoperation.getAuth("authToken", new AuthData(authToken, null)); // 因为只用authToken搜索. 所以username 是null
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, IllegalAccessException {
        dBoperation.delete_auth("authToken", new AuthData(authToken, null));
    }

    /*调用getAuth来看这个authToken是不是在数据库里 在返回true 不在返回false*/
    @Override
    public boolean auth_is_stored(String authToken) throws DataAccessException, IllegalAccessException {
        return getAuth(authToken) != null;
    }

    public String get_user_name(String authToken) throws IllegalAccessException {
        try
        {
            AuthData my_auth = getAuth(authToken);
            return my_auth.username();
        }
        catch (Exception e)
        {
            throw new IllegalAccessException("Error: unauthorized");
        }

    }

}
