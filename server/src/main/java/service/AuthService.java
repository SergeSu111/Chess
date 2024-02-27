package service;

import dataAccess.*;
import model.AuthData;
import request.RegisterRequest;
import result.RegisterResult;

public class AuthService {
    private AuthDAO authDAO = new MemoryAuth();
    private UserDAO userDAO = new MemoryUser();

    public String create_auth(String username) throws DataAccessException {

        return authDAO.createAuth(username);
    }

    public AuthData get_auth(String auth_token) throws DataAccessException, IllegalAccessException {
        return authDAO.getAuth(auth_token, authDAO.createAuth(username));
    }

    public void delete_auth(String auth_token) throws DataAccessException, IllegalAccessException {
        authDAO.deleteAuth(auth_token);
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, IllegalAccessException {
       if (userDAO.user_is_stored(request.username()))
       {
           throw new DataAccessException("The username has already been stored");
       }
       else
       {
           userDAO.create_user(request.username(), request.password(), request.email());
           String new_token = authDAO.createAuth(request.username());
           return new RegisterResult(request.username(), new_token); // 返回一个新的用户的token
       }
    }

}
