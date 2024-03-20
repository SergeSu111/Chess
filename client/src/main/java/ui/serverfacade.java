package ui;


import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;

import java.net.HttpURLConnection;

public class serverfacade {
    private final String serverUrl;

    public serverfacade(String url)
    {
        serverUrl = url; // the url is localhost 8080?
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException
    {
        RegisterResult response = null;
        try
        {
            String body = new Gson().toJson(request); // 先将register请求变成json
            HttpURLConnection connection =makeHTTPRequest("POST", "/user", request, RegisterResult.class);
        }
    }


    private <T>  T makeHTTPRequest (String method, String path, Object request, Class<T> responseClass)
    {

    }

}
