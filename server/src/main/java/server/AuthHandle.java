package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;
import service.AuthService;
import spark.Request;
import spark.Response;

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
            result_back = new Gson().toJson(RegisterResult);
            this.response.status(200);
        } catch (DataAccessException e) {
            result_back = new Gson().toJson()
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return new Gson().toJson("Serge");
    }

    public Object login(String username, String password)
    {
        LoginRequest login_request = get_body(this.request, LoginRequest.class);
        return new Gson().toJson("Serge");
    }

    public Object logout()
    {
        return new Gson().toJson("Serge");
    }
}
