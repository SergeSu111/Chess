package service;

import dataAccess.*;
import model.AuthData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.sql.SQLException;

public class AuthService {
    private AuthDAO authDAO = new sqlAuth();
    private UserDAO userDAO = new sqlUser();

    public AuthService() throws DataAccessException {
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, IllegalAccessException {
       if (userDAO.userIsStored(request.username()))
       {
           throw new DataAccessException("Error: already taken");
       }
       else
       {
           userDAO.createUser(request.username(), request.password(), request.email());
           String newToken = authDAO.createAuth(request.username());
           return new RegisterResult(request.username(), newToken); // 返回一个新的用户的token
       }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, IllegalAccessException, SQLException {
        if (userDAO.userIsStored(loginRequest.username()))
        {
            if (loginRequest.password() == null || loginRequest.username() == null)
            {
                throw new DataAccessException("Error: bad request");
            }
            if (userDAO.passwordMatch(loginRequest.username(), loginRequest.password()))
            {
                String theAuthToken = authDAO.createAuth(loginRequest.username()); // 创建一个login的token
                return new LoginResult(loginRequest.username(), theAuthToken); // 返回这个result
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
        if (authDAO.authIsStored(authToken))
        {
            authDAO.deleteAuth(authToken);
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }


}
