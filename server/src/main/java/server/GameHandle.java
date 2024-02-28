package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGameRequest;
import result.CreateGameResult;
import result.ListGameResult;
import result.ServerResult;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Objects;

// listGames, createGame, joinGame,
public class GameHandle extends ServiceHandle{

    private final GameService gameService = new GameService(); // 把gameService拿过来
    public GameHandle(Request request, Response response)
    {
        super(request, response);
    }

    public Object createGame()
    {
        String result_back;
        String authToken = this.request.headers("authorization");
        CreateGameRequest create_game_request = get_body(this.request, CreateGameRequest.class);
        try
        {
            CreateGameResult createGameResult = this.gameService.createGame(create_game_request, authToken);
            result_back = new Gson().toJson(createGameResult);
            this.response.status(200);
        }
        catch (DataAccessException e)
        {
            result_back = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex)
        {
            result_back = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return result_back;
    }

    public Object listGame()
    {
        String result_back;
        String auth_Token = this.request.headers("authorization");
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

    public Object joinGame() {
        String result_back;
        String authToken = this.request.headers("authorization");
        JoinGameRequest join_game_request = get_body(this.request, JoinGameRequest.class);
        try {
            this.gameService.join_game(join_game_request, authToken);
            result_back = new Gson().toJson(new ServerResult(""));
            this.response.status(200);
        } catch (DataAccessException | IllegalAccessException ex) {
            result_back = new Gson().toJson(new ServerResult(ex.getMessage()));
            if (Objects.equals(ex.getMessage(), "Error: bad request")) {
                this.response.status(400);
            } else if (Objects.equals(ex.getMessage(), "Error: unauthorized")) {
                this.response.status(401);
            } else if (Objects.equals(ex.getMessage(), "Error: already taken")) {
                this.response.status(403);
            } else {
                this.response.status(418);
            }
        } catch (Exception e)
        {
            result_back = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return result_back;
    }



}
