package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpConnect {
    private final int port;
    private final String url;

    public HttpConnect(int port, String url) {
        this.port = port;
        this.url = "http://" + url;
    }

    public HttpURLConnection makeHTTPRequest(String path, String method, String body, String auth) throws URISyntaxException, IOException {
        String preUrl = getRequestedURL(path);
        URI urI = new URI(preUrl);
        HttpURLConnection http = (HttpURLConnection) urI.toURL().openConnection();
        http.setRequestMethod(method);
        if (!(auth == null)) {
            http.setRequestProperty("authorization", auth);
        }
        writeHttpReqBody(http, body);
        http.connect();
        return http;
    }

    public HttpURLConnection makeHTTPRequest(String path, String method, String body) throws URISyntaxException, IOException {
        return makeHTTPRequest(path, method, body, null);
    }

    private String getRequestedURL(String path) {
        return this.url + this.port + path;
    }

    private static void writeHttpReqBody(HttpURLConnection http, String body) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (OutputStream outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    public static boolean hasGoodResponseCode(HttpURLConnection http) throws IOException {
        return http.getResponseCode() == 200;
    }

    public static void throwResponseError(HttpURLConnection http) throws IOException, ResponseException {
        throw new ResponseException("Server returned: " + http.getResponseCode() + " " + http.getResponseMessage());
    }

    public static <T> T getHTTPResponseBody (HttpURLConnection http, Class<T> clas) throws IOException {
        T responseBody;
        try (InputStream inputStream = http.getInputStream())
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            responseBody = new Gson().fromJson(inputStreamReader, clas);
        }
        return responseBody;
    }







}
