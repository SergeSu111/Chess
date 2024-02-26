package server;
import spark.*; // 用于生产处理请求的request对象和生产响应的response对象
import com.google.gson.Gson;
public class ServiceHandle {

    // protected可以让继承的类也使用其属性.
    protected Request request;
    protected Response response;

    //constructor
    public ServiceHandle(Request request, Response response)
    {
        this.request = request;
        this.response = response;
    }

    /*get the request body and make the json content in the request body into java class,
    * we use template */
    protected static <T> T get_body(Request request, Class<T> my_class)
    {
        // var is auto in C++. make the request body bee my_class type, and give it to body
        var body = new Gson().fromJson(request.body(), my_class);
        if (body == null)
        {
            throw new RuntimeException("You need to add something in your request.");
        }
        return body; // could be null or something else
    }
}


// 创建一个在服务器里的服务处理. 所有处理endpoints的service方法都将继承这个类. 来得到不同endpoints请求的request，
// 然后放入各自的endpoints方法里来处理这个请求.