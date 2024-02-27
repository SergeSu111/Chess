package server;

import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGameRequest;
import spark.Request;
import spark.Response;

// listGames, createGame, joinGame,
public class GameHandle extends ServiceHandle{
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
        ListGameRequest list_game_request = get_body(this.request, ListGameRequest.class);
        return new Gson().toJson(list_game_request);
    }

    public Object joinGame()
    {
        JoinGameRequest join_game_request = get_body(this.request, JoinGameRequest.class);
        return new Gson().toJson(join_game_request);
    }



}
