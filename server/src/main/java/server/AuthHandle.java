package server;

import com.google.gson.Gson;
import request.LoginRequest;
import request.RegisterRequest;
import spark.Request;
import spark.Response;

// handle endpoints for register , login , logout
public class AuthHandle extends ServiceHandle
{
    public AuthHandle(Request request, Response response)
    {
        super(request, response); // inherit the request and response from ServiceHandle
    }

    public Object register()
    {
        // make the json request from ServiceHandle change into RegisterRequest class,
        // and give it to request object.
        RegisterRequest register_request = get_body(this.request, RegisterRequest.class);
        return new Gson().toJson("Serge");
    }

    public Object login()
    {
        LoginRequest login_request = get_body(this.request, LoginRequest.class);
        return new Gson().toJson("Serge");
    }

    public Object logout()
    {
        return new Gson().toJson("Serge");
    }
}
