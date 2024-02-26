package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void clear() throws DataAccessException;  // 万一没有东西clear 就throw exception

    public void createAuth() throws DataAccessException;

    public AuthData getAuth() throws DataAccessException;

    public void deleteAuth() throws DataAccessException;
}
