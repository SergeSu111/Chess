package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import service.ClearService;
import spark.*;


/*This class is for the service of processing clear endpoints*/
public class ClearOperation extends ServiceHandle
{
    ClearService myClearService = new ClearService();
    public ClearOperation(Request request, Response response)
    {
        super(request, response);
        // 既然要处理clear的endpoints 肯定需要clear 的request内容.
        // 继承了ServiceHandle来的得到request的属性.
    }

    public Object clear () throws DataAccessException {
        myClearService.clear();

        this.response.status(200); // 200 表面http request成功了
        return "{}";
    }

}
