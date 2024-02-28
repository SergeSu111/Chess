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
    public AuthHandle(Request request, Response response)
    {
        super(request, response); // inherit the request and response from ServiceHandle
    }

    public Object register()
    {
        // make the json request from ServiceHandle change into RegisterRequest class,
        // and give it to request object.
        String result_back;  // 放入json 转回给用户的
        RegisterRequest register_request = get_body(this.request, RegisterRequest.class);
        try
        {
            RegisterResult register_response = this.authService.register(register_request); // call service and call Dataacess
            result_back = new Gson().toJson(register_response);
            this.response.status(200);
        }
        catch (DataAccessException e) {
            result_back = new Gson().toJson(new ServerResult(e.getMessage())); // 如果上面返回的json哪一步有问题 就把异常打出来放入ServerResult 然后给json
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
            result_back = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return result_back;
    }

    public Object login() throws DataAccessException, IllegalAccessException {
        String result_back;
        LoginRequest login_request = get_body(this.request, LoginRequest.class);
        try
        {
            LoginResult login_result = this.authService.login(login_request);
            result_back = new Gson().toJson(login_result);
            this.response.status(200);
        }
        catch (DataAccessException e)
        {
            result_back = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex)
        {
            result_back = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return result_back;

    }

    public Object logout()
    {
        String result_back;
        String auth_token = this.request.headers().toString(); //把在header里的auth_token拿过来
        try
        {
            this.authService.logout(auth_token);
            result_back = new Gson().toJson(new ServerResult("")); // logout只是删除 所以ServerResult啥都没有
            this.response.status(200);
        }
        catch (DataAccessException | IllegalAccessException e)
        {
            result_back = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch(Exception ex)
        {
            result_back = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return result_back;
    }
}
