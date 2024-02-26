package server;

import spark.Request;
import spark.Response;

// handle endpoints for register , login , logout
public class AuthHandle extends ServiceHandle
{
    public AuthHandle(Request request, Response response)
    {
        super(request, response); // inherit the request and response from ServiceHandle
    }



}
