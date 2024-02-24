package server;

import com.google.gson.Gson;
import request.RegisterRequest;
import service.UserService;
import spark.*;  //导入Spark框架来创建java web 服务器

public class Server {

    private final UserService service = new UserService(); // 将service拿过来
    public int run(int desiredPort) { //启动服务器的方法 接受一个参数端口 80
        Spark.port(desiredPort);   // 将服务器端口设置为传入的参数 80

        Spark.staticFiles.location("web"); // 设置静态文件的基本目录名为web 任何在这个目录下的文件都可以用http访问

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear); // spark.http method(url path, function)  注册了delete请求处理器. 当路径为/db
        //的时候, 就调用Server里的clear function. this::clear 代表引用Server类的clear函数
        Spark.post("/user", this::register);

        Spark.awaitInitialization();  // 这行代码会等待服务器初始化完成.  js promise
        return Spark.port(); // 返回实际使用的端口
    }
    private Object clear(Request request, Response response)
    {
        service.clear(); // 从service 里直接调用clear
        response.status(200);
        return "{}"; // where the return returns to?
    }

    private Object register(Request request, Response response){

           // what this line means?
          RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class); // input to register()
          service.register(registerRequest);
        return null;


    }

    public void stop() {  // 停止服务器的方法
        Spark.stop();  // 停止spark服务器运行
        Spark.awaitStop(); // 等待服务器完全停止然后返回
    }
}
