package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGame;
import model.GameData;
import request.CreateGameRequest;

import java.util.Collection;

public class GameService
{
    private GameDAO gameDAO = new MemoryGame();

    public Collection<GameData> listGame() throws DataAccessException {
        return gameDAO.listGames();
    }

    public int createGame(CreateGameRequest create_game_request)
    {
        return gameDAO.createGame(create_game_request.gameName())
    }


}
