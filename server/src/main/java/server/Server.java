package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;

import spark.*;  //导入Spark框架来创建java web 服务器

public class Server {

    public int run(int desiredPort) { //启动服务器的方法 接受一个参数端口 80
        Spark.port(desiredPort);   // 将服务器端口设置为传入的参数 80

        Spark.staticFiles.location("web"); // 设置静态文件的基本目录名为web 任何在这个目录下的文件都可以用http访问

        // call all your endpoints methods to handle them

        // 使用spark.delete方法来request delete方式的http请求. 参数1是请求的url. db是clear的url. 然后使用匿名函数,
        // 传入spark里的request 和response 返回一个返回给用户的http RESPONSE. 一般是json的形式.
        Spark.delete("/db", ((request, response) -> new ClearOperation(request, response).clear()));

        Spark.awaitInitialization();  // 这行代码会等待服务器初始化完成.  js promise
        return Spark.port(); // 返回实际使用的端口
    }



    public void stop() {  // 停止服务器的方法
        Spark.stop();  // 停止spark服务器运行
        Spark.awaitStop(); // 等待服务器完全停止然后返回
    }
}
