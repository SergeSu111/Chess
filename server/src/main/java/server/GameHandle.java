package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGameRequest;
import result.ListGameResult;
import result.ServerResult;
import service.GameService;
import spark.Request;
import spark.Response;

// listGames, createGame, joinGame,
public class GameHandle extends ServiceHandle{

    private GameService gameService = new GameService(); // 把gameService拿过来
    public GameHandle(Request request, Response response)
    {
        super(request, response);
    }

    public Object createGame()
    {
        CreateGameRequest create_game_request = get_body(this.request, CreateGameRequest.class);
        return new Gson().toJson(create_game_request);
    }

    public Object listGame()
    {
        String result_back;
        String auth_Token = this.request.headers().toString();
        try {
            ListGameResult listGameResult = this.gameService.listGame(auth_Token);
            result_back = new Gson().toJson(listGameResult);
            this.response.status(200);
        }
        catch(DataAccessException e)
        {
            result_back = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch(Exception ex)
        {
            result_back = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return result_back;

    }

    public Object joinGame()
    {
        JoinGameRequest join_game_request = get_body(this.request, JoinGameRequest.class);
        return new Gson().toJson(join_game_request);
    }



}
