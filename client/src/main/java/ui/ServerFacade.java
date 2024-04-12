package ui;



import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;
import server.ClearOperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    public String authToken;
    public ServerFacade(String url){
        serverUrl = url;
    }

    public void register(String username, String password, String email) throws ResponseException, DataAccessException {
        String path = "/user";
        RegisterRequest requestBody =  new RegisterRequest(username, password, email);
        RegisterResult registerResult = this.makeRequest("POST", path, requestBody, RegisterResult.class);
        authToken = registerResult.authToken();
    }



    public LoginResult login(String username, String password) throws ResponseException, DataAccessException {
        var path = "/session";
        LoginRequest requestBody = new LoginRequest(username, password);
        LoginResult response =  this.makeRequest("POST", path, requestBody, LoginResult.class);
        authToken = response.authToken();
        return response;
    }
    public void logout() throws ResponseException, DataAccessException {
        String path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public ArrayList<ListGameInformation> listGames() throws ResponseException, DataAccessException {
        String path = "/game";
        var response =  this.makeRequest("GET", path, null, ListGameResult.class);
        return response.games();
    }

    public CreateGameResult createGame(String gameName) throws ResponseException, DataAccessException {
        String path = "/game";
        CreateGameRequest request = new CreateGameRequest(gameName);
        return this.makeRequest("POST",path,request, CreateGameResult.class);
    }

    public void joinGame(int gameID, String color) throws ResponseException, DataAccessException {
        String path = "/game";
        JoinGameRequest request = new JoinGameRequest(color, gameID);
        this.makeRequest("PUT", path, request, null);
    }

    public String getAuthToken() {
        return authToken;
    }


    public void clear() throws ResponseException, DataAccessException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, ClearOperation.class);
    }

    private <T> T makeRequest(String method, String path, Object request , Class<T> responseClass) throws DataAccessException {
        try{
            URI uri = new URI(serverUrl+path);
//      URI uri = new URI("http://localhost:8080/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            if (authToken != null){
                http.addRequestProperty("authorization", authToken);
            }
            writeRequestBody(request, http);
            http.connect();

            T response =  readResponseBody(http,responseClass);
            return response;
        }
        catch(IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }

    }
    private static void writeRequestBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.setDoOutput(true);
            var jsonBody = new Gson().toJson(request);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(jsonBody.getBytes());
            }
        }
    }
    private static <T> T  readResponseBody(HttpURLConnection http,Class<T> responseClass) throws DataAccessException {
        T responseBody = null;
        try{
            if(http.getResponseCode() == 200) {


                InputStream respBody=http.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(respBody);
                if(responseClass != null) {
                    responseBody=new Gson().fromJson(inputStreamReader, responseClass);
                }else{
                    return null;
                }


                return responseBody;
            }
            else{
                InputStream respBody=http.getErrorStream();
                InputStreamReader inputStreamReader=new InputStreamReader(respBody);
                var response=new Gson().fromJson(inputStreamReader, Map.class);
                throw new DataAccessException((String) response.get("message"));
            }
        }
        catch(IOException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }




















    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}