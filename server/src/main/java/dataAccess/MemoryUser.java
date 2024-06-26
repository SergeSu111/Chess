package dataAccess;

import chess.ChessGame;
import model.UserData;

public class MemoryUser implements UserDAO{

    private final DBoperation dBoperation = new DBoperation();
    @Override
    public void clear() {
        dBoperation.clearUser();
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        if (!(username == null || password == null || email == null ))
        {
            dBoperation.createUser(username, password, email);
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }

    }

    @Override
    public UserData getUser(String username, String password, String email) throws DataAccessException, IllegalAccessException {
       return dBoperation.getUser("username", new UserData(username, password, email));
    }

    @Override
    public boolean userIsStored(String username) throws DataAccessException, IllegalAccessException {
        return getUser(username, null, null) != null;
    }

    @Override
    public boolean passwordMatch(String testUsername, String password) throws DataAccessException, IllegalAccessException {
        UserData theUser = getUser(testUsername, password, null);
        if (theUser != null)
        {
            return password.equals(theUser.password());
        }
        else
        {
            return false;
        }
    }

    @Override
    public void removeUser(ChessGame.TeamColor color, int gameID) throws DataAccessException {

    }

    @Override
    public void configureDatabase() throws DataAccessException {

    }
}
