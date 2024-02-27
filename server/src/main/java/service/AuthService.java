package service;

import dataAccess.*;
import model.AuthData;
import request.RegisterRequest;
import result.RegisterResult;

public class AuthService {
    private AuthDAO authDAO = new MemoryAuth();
    private UserDAO userDAO = new MemoryUser();

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
