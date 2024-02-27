package dataAccess;

import model.UserData;

public class MemoryUser implements UserDAO{

    DBoperation dBoperation = new DBoperation();
    @Override
    public void clear() {
        dBoperation.clear_user();
    }

    @Override
    public void create_user(String username, String password, String email) throws DataAccessException {
        dBoperation.create_user(username, password, email);
    }

    @Override
    public UserData get_user(String username) throws DataAccessException, IllegalAccessException {
       return dBoperation.getUser("username", new UserData(username, null, null));
    }

    @Override
    public boolean user_is_stored(String username) throws DataAccessException, IllegalAccessException {
        if (get_user(username) == null) {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean password_match(String testUsername, String password) throws DataAccessException, IllegalAccessException {
        UserData the_user = get_user(testUsername);
        if (the_user != null)
        {
            return password.equals(the_user.password());
        }
        else
        {
            return false;
        }
    }
}
