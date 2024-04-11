package ui;

import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGameResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;


public class ServerFacade {
    private final int serverPort;
    private final String urlStemLocal;

    public String authToken;
    public ServerFacade(int serverPort) {
        this.serverPort = serverPort;
        urlStemLocal = "http://localhost:";
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        RegisterResult response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/user", "POST", body);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, RegisterResult.class); }
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
        return response;
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        LoginResult response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/session", "POST", body);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, LoginResult.class); }  // response is null
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
        authToken = response.authToken();
        return response;
    }

    public void logout() throws ResponseException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/session", "DELETE", "", authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
    }

    public ListGameResult listGames() throws ResponseException {
        ListGameResult response;
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "GET", "", authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            response = getHTTPResponseBody(connection, ListGameResult.class);
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
        return response;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        CreateGameResult response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "POST", body, authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, CreateGameResult.class);}
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
        return response;
    }

    public void joinGame(JoinGameRequest request) throws ResponseException {
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "PUT", body, authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
    }

    public void clear() throws ResponseException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/db", "DELETE", "");
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
        } catch (Exception ex) { throw new ResponseException(ex.getMessage()); }
    }

    private HttpURLConnection makeHTTPRequest(int port, String urlStem, String path, String method, String body, String authHeader) throws URISyntaxException, IOException {
        String preURI = weldURLComponents(port, urlStem, path);
        URI uri = new URI(preURI);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        if (!(authHeader == null)) { http.setRequestProperty("authorization", authHeader); }
        writeHTTPRequestBody(http, body);
        http.connect();
        return http;
    }

    private String weldURLComponents(int port, String urlStem, String path) { return urlStem + port + path; }

    private HttpURLConnection makeHTTPRequest(int port, String urlStem, String path, String method, String body) throws URISyntaxException, IOException {
        return makeHTTPRequest(port, urlStem, path, method, body, null);
    }

    private static void writeHTTPRequestBody(HttpURLConnection http, String body) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (OutputStream outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static boolean hasGoodResponseCode(HttpURLConnection http) throws IOException {
        return http.getResponseCode() == 200;
    }

    private static void throwResponseError(HttpURLConnection http) throws IOException, ResponseException {
        throw new ResponseException("Server returned: " + http.getResponseCode() + " " + http.getResponseMessage());
    }

    private static <T> T getHTTPResponseBody(HttpURLConnection http, Class<T> clazz) throws IOException {
        T responseBody;
        try (InputStream inputStream = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            responseBody = new Gson().fromJson(inputStreamReader, clazz);
        }
        return responseBody;
    }
}