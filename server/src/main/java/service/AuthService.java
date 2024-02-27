package service;

import dataAccess.*;
import model.AuthData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class AuthService {
    private AuthDAO authDAO = new MemoryAuth();
    private UserDAO userDAO = new MemoryUser();

    public RegisterResult register(RegisterRequest request) throws DataAccessException, IllegalAccessException {
       if (userDAO.user_is_stored(request.username()))
       {
           throw new DataAccessException("Error: already taken");
       }
       else
       {
           userDAO.create_user(request.username(), request.password(), request.email());
           String new_token = authDAO.createAuth(request.username());
           return new RegisterResult(request.username(), new_token); // 返回一个新的用户的token
       }
    }

    public LoginResult login(LoginRequest login_request) throws DataAccessException, IllegalAccessException {
        if (userDAO.user_is_stored(login_request.username()))
        {
            if (userDAO.password_match(login_request.username(), login_request.password()))
            {
                String the_auth_token = authDAO.createAuth(login_request.username()); // 创建一个login的token
                return new LoginResult(login_request.username(), the_auth_token); // 返回这个result
            }
            else
            {
                throw new DataAccessException("Error: unauthorized");
            }
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout (String authToken) throws DataAccessException, IllegalAccessException {
        if (authDAO.auth_is_stored(authToken))
        {
            authDAO.deleteAuth(authToken);
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }


}
