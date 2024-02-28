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

public class GameService {
    private final GameDAO gameDAO = new MemoryGame();

    private final AuthDAO authDAO = new MemoryAuth();

    public ListGameResult listGame(String authToken) throws DataAccessException, IllegalAccessException {
        if (authDAO.auth_is_stored(authToken))
        {
            ArrayList<ListGameInformation> all_games = new ArrayList<>();
            ArrayList<GameData> gameList = (ArrayList<GameData>) gameDAO.listGames();
            for (GameData game : gameList)
            {
                // 将每一个游戏都放入游戏信息里
                all_games.add(new ListGameInformation(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }
            return new ListGameResult(all_games);
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest create_game_request, String auth_token) throws DataAccessException, IllegalAccessException {
        if (authDAO.auth_is_stored(auth_token))
        {
            return new CreateGameResult(gameDAO.createGame(create_game_request.gameName()));
        }
        else
        {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void join_game(JoinGameRequest join_game_request, String authToken) throws DataAccessException, IllegalAccessException {
        if (authDAO.auth_is_stored(authToken))
        {
            String username = authDAO.get_user_name(authToken);
            if (gameDAO.game_exists(join_game_request.gameID()))
            {
                if (!(join_game_request.PlayerColor() == null))
                {
                    try
                    {
                        gameDAO.join_game(join_game_request.gameID(), username, join_game_request.PlayerColor());
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
