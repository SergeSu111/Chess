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

import java.util.ArrayList;
import java.util.Objects;

// listGames, createGame, joinGame,
public class GameHandle extends ServiceHandle{

    private final GameService gameService = new GameService(); // 把gameService拿过来
    public GameHandle(Request request, Response response) throws DataAccessException {
        super(request, response);
    }

    public Object createGame()
    {
        String resultBack;
        String authToken = this.request.headers("authorization");
        CreateGameRequest createGameRequest = getBody(this.request, CreateGameRequest.class);
        try
        {
            CreateGameResult createGameResult = this.gameService.createGame(createGameRequest, authToken);
            resultBack = new Gson().toJson(createGameResult);
            this.response.status(200);
        }
        catch (DataAccessException e)
        {
            resultBack = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex)
        {
            resultBack = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return resultBack;
    }

    public Object listGame()
    {
        String resultBack;
        String authToken = this.request.headers("authorization");
        try {
            ListGameResult listGameResult = this.gameService.listGame(authToken);
            resultBack = new Gson().toJson(listGameResult);
            this.response.status(200);
        }
        catch(DataAccessException e)
        {
            resultBack = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(401);
        }
        catch(Exception ex)
        {
            resultBack = new Gson().toJson(new ServerResult(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return resultBack;
    }

    public Object joinGame() {
        String resultBack;
        String authToken = this.request.headers("authorization");
        JoinGameRequest joinGameRequest = getBody(this.request, JoinGameRequest.class);
        try {
            this.gameService.joinGame(joinGameRequest, authToken);
            resultBack = new Gson().toJson(new ServerResult(""));
            this.response.status(200);
        } catch (DataAccessException | IllegalAccessException ex) {
            resultBack = new Gson().toJson(new ServerResult(ex.getMessage()));
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
            resultBack = new Gson().toJson(new ServerResult(e.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return resultBack;
    }



}
