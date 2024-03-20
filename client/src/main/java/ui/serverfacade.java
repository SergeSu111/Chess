package ui;


import com.google.gson.Gson;
import request.*;
import result.CreateGameResult;
import result.ListGameResult;
import result.LoginResult;
import result.RegisterResult;

import java.net.HttpURLConnection;

public class serverfacade {
    private final String serverUrl;

    public serverfacade(String url) {
        serverUrl = url; // the url is localhost 8080?
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeHTTPRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeHTTPRequest("POST", path, request, LoginResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeHTTPRequest("DELETE", path, null, null);
    }

    public ListGameResult listGames(ListGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeHTTPRequest("GET", path, request, ListGameResult.class);
    }

    public CreateGameResult createGames(CreateGameRequest request) throws ResponseException
    {
        var path = "/game";
        return this.makeHTTPRequest("POST", path, request, CreateGameResult.class);
    }

    public void JoinGame (JoinGameRequest request) throws ResponseException
    {
        var path = "/game";
        this.makeHTTPRequest("PUT", path, request, null);
    }

    public void clear() throws ResponseException
    {
        var path = "/db";
        this.makeHTTPRequest("DELETE", path, null, null);
    }

    private <T> T makeHTTPRequest(String method, String path, Object request, Class<T> responseClass)
    {

    }

}
