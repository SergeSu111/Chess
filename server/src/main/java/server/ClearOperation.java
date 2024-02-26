package server;

import com.google.gson.Gson;
import spark.*;


/*This class is for the service of processing clear endpoints*/
public class ClearOperation extends ServiceHandle
{
    public ClearOperation(Request request, Response response)
    {
        super(request, response);
        // 既然要处理clear的endpoints 肯定需要clear 的request内容.
        // 继承了ServiceHandle来的得到request的属性.
    }

    public Object clear ()
    {
        this.response.status(200); // 200 表面http request成功了
        this.response.type("application/json"); // 表示反馈给客户的请求是json的类型
        return new Gson().toJson(200); // 返回给用户json反馈是200
    }

}
