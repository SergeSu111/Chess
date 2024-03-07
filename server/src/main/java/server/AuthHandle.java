package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import result.ServerResult;
import service.AuthService;
import spark.Request;
import spark.Response;

import java.util.Objects;

// handle endpoints for register , login , logout
public class AuthHandle extends ServiceHandle
{
    private AuthService authService = new AuthService();
    public AuthHandle(Request request, Response response) throws DataAccessException {
        super(request, response); // inherit the request and response from ServiceHandle
    }

    public Object register()
    {
        // make the json request from ServiceHandle change into RegisterRequest class,
        // and give it to request object.
        String resultBack;  // 放入json 转回给用户的
        RegisterRequest registerRequest = getBody(this.request, RegisterRequest.class);
        try
        {
            RegisterResult registerResponse = this.authService.register(registerRequest); // call service and call Dataacess
            resultBack = new Gson().toJson(registerResponse);
            this.response.status(200);
        }
        catch (DataAccessException e) {
            resultBack = new Gson().toJson(new ServerResult(e.getMessage())); // 如果上面返回的json哪一步有问题 就把异常打出来放入ServerResult 然后给json
            if (Objects.equals(e.getMessage(), "Error: bad request"))
            {
                this.response.status(400);
            }
            else if (Objects.equals(e.getMessage(), "Error: already taken"))
            {
                this.response.status(403);
            }
            else
            {
                this.response.status(418);
            }
        }
        catch (Exception ex)
        {
            resultBack = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return resultBack;
    }

    public Object login() throws DataAccessException, IllegalAccessException {
        String resultBack;
        LoginRequest loginRequest = getBody(this.request, LoginRequest.class);
        try
        {
            LoginResult loginResult = this.authService.login(loginRequest);
            resultBack = new Gson().toJson(loginResult);
            this.response.status(200);
        }
        catch (DataAccessException e)
        {
            resultBack = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex)
        {
            resultBack = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);

        }
        this.response.type("application/json");
        return resultBack;

    }

    public Object logout()
    {
        String resultBack;
        String authToken = this.request.headers("authorization"); //把在header里的auth_token拿过来
        try
        {
            this.authService.logout(authToken);
            resultBack = new Gson().toJson(new ServerResult("")); // logout只是删除 所以ServerResult啥都没有
            this.response.status(200);
        }
        catch (DataAccessException | IllegalAccessException e)
        {
            resultBack = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch(Exception ex)
        {
            resultBack = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return resultBack;
    }
}
