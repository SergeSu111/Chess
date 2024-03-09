package dataAccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    void clear() throws DataAccessException, SQLException;
    void createUser(String username, String password, String email) throws DataAccessException;
    UserData getUser(String username, String password, String email) throws DataAccessException, IllegalAccessException;

    boolean userIsStored(String username) throws DataAccessException, IllegalAccessException;

    boolean passwordMatch(String testUsername, String password) throws DataAccessException, IllegalAccessException, SQLException;

    void configureDatabase() throws DataAccessException;
}
