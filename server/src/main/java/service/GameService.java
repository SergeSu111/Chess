package service;

import dataAccess.*;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGameRequest;
import result.CreateGameResult;
import result.ListGameInformation;
import result.ListGameResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class GameService {
    private final GameDAO gameDAO = new MemoryGame();

    private final AuthDAO authDAO = new MemoryAuth();

    public ListGameResult listGame(String authToken) throws DataAccessException, IllegalAccessException {
        if (authDAO.authIsStored(authToken))
        {
            ArrayList<ListGameInformation> allGames = new ArrayList<>();
            HashSet<GameData> gameList = gameDAO.listGames();
            for (GameData game : gameList)
            {
                // 将每一个游戏都放入游戏信息里
                allGames.add(new ListGameInformation(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }
            return new ListGameResult(allGames);
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException, IllegalAccessException {
        if (authDAO.authIsStored(authToken))
        {
            return new CreateGameResult(gameDAO.createGame(createGameRequest.gameName()));
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException, IllegalAccessException {
        if (authDAO.authIsStored(authToken))
        {
            String username = authDAO.getUserName(authToken);
            if (gameDAO.gameExists(joinGameRequest.gameID()))
            {
                if (!(joinGameRequest.playerColor() == null))
                {
                    try
                    {
                        gameDAO.joinGame(joinGameRequest.gameID(), username, joinGameRequest.playerColor());
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new DataAccessException("Error: bad request");
                    }
                }
            }
            else
            {
                throw new DataAccessException("Error: bad request");
            }

        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
