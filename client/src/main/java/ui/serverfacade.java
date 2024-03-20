package ui;


import com.google.gson.Gson;
import request.*;
import result.CreateGameResult;
import result.ListGameResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

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

    public CreateGameResult createGames(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeHTTPRequest("POST", path, request, CreateGameResult.class);
    }

    public void JoinGame(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        this.makeHTTPRequest("PUT", path, request, null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeHTTPRequest("DELETE", path, null, null);
    }

    private <T> T makeHTTPRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();  // 将请求的path转变成URL, 因为HTTP请求的地址通常是URL表示的
            // 通过open Connection()的方法来打开连接到服务器的HTTP连接 创建了可以连接到服务器的HTTP连接.是关于这个特定的URL的HTTP连接
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method); // 将这个http设置一个请求方法
            http.setDoOutput(true); // 指明该连接对象将会output一个request到server 包括给server发送数据等

            writeBody(request, http); // 用于将请求体request写入到http连接对象中
            http.connect(); // 前面的都是配置 这行才是真正的将http与服务器进行连接
            throwIfNotSuccessful(http);  // 检查HTTP是否响应成功 如果不成功抛出异常
            return readyBody(http, responseClass); // 如果响应成功 则从http中读取response. 并将其转换为相应的类型. 最后返回给调用者
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) // 如果request不为空
        {
            http.addRequestProperty("Content-Type", "application/json"); // 那么就把这个request加到http里 改成json的模式传 先设置了request的类型属性
            String reqData = new Gson().toJson(request); // 然后将request转成json类型 给到reqData
            try (OutputStream reqBody = http.getOutputStream()) // 大致意思应该是通过这个函数得到了reqBody
            {
                reqBody.write(reqData.getBytes()); // 将request data 写入这个body里
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode(); // 因为http要连接到服务器 所以肯定会返回一个status code
        if (!isSuccessful(status)) // 如果code不是成功的code
        {
            throw new ResponseException(status, "failure: " + status); // 报告异常
        }
    }

    /*既然返回成功 那么给我返回的东西*/
    private static <T> T readyBody(HttpURLConnection http, Class<T> responseClass) throws IOException
    {
        T response = null;
        // 如果响应体长度小于0. 证明长度为止或无法确定. 在这种情况下, 响应体 应该是可读的
        // 因为无法确定长度 所以需要读取整个响应体以获取长度
        if (http.getContentLength() < 0)
        {
            // 使用了try with-resources语句 打开了一个输入流resBody来读取http的响应体 当读取完之后关掉它
            try (InputStream respBody = http.getInputStream())
            {
                // 创建一个reader来从resBody中读取字符串.
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null)
                {
                    // reader最后读取出来了响应后的response 然后转换为responseClass
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status)
    {
        return status / 100 == 2;
    }

}
