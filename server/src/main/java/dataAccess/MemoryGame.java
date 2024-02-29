package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MemoryGame  implements GameDAO{

    private final DBoperation dBoperation = new DBoperation(); // create the object of DB operation


    @Override
    public int createGame(String game_name) {
        int theGameId = dBoperation.getNewGameId();
        dBoperation.createGame(theGameId, null, null, game_name, null);
        return theGameId;
    }

    @Override
    public GameData getGame(int gameID) throws IllegalAccessException {
        return dBoperation.getGame("gameID", new GameData(gameID, null, null, null,null));
    }

    @Override
    public HashSet<GameData> listGames() {
        return dBoperation.showAllGames();
    }

    @Override
    public void updateGame(int gameID, String game) throws DataAccessException, IllegalAccessException {
       GameData updated = dBoperation.getGame("gameID", new GameData(gameID, null, null, null, null));
       if (updated == null) {throw new DataAccessException("Error: bad request");}
       else {
           dBoperation.delGame("all", updated);
           dBoperation.createGame(updated.gameID(), updated.whiteUsername(), updated.blackUsername(), updated.gameName(), game);
       }
    }

    @Override
    public void clear() {
        dBoperation.clearGame();
    }

    @Override
    public boolean gameExists(int game_id) throws IllegalAccessException {
        return getGame(game_id) != null;
    }

    @Override
    public void updatePlayers(int gameID, String username, String the_color) throws IllegalAccessException, DataAccessException
    {
        GameData forUpdate = dBoperation.getGame("gameID", new GameData(gameID, null, null, null, null));
        if (forUpdate == null) {throw new DataAccessException("Error: bad request");}
        else
        {
            dBoperation.delGame("all", forUpdate);
            if (the_color.equals("WHITE"))
            {
                dBoperation.createGame(forUpdate.gameID(), username, forUpdate.blackUsername(), forUpdate.gameName(), forUpdate.game());
            }
            else if (the_color.equals("BLACK"))
            {
                dBoperation.createGame(forUpdate.gameID(), forUpdate.whiteUsername(), username, forUpdate.gameName(),forUpdate.game());
            }
            else
            {
                throw new IllegalAccessException("Error: invalid color.");
            }
        }

    }

    @Override
    public void joinGame(int gameID, String username, String the_color) throws DataAccessException, IllegalAccessException {
        boolean colorGood;
        try
        {
            colorGood = colorFree(the_color, gameID); // if got error
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Error: bad request");
        }
        if (colorGood) // if color is available
        {
            updatePlayers(gameID, username, the_color);
        }
        else
        {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public boolean colorFree(String the_color, int gameID) throws DataAccessException, IllegalAccessException {
        if (gameExists(gameID))
        {
            GameData gameCheck = getGame(gameID);
            return gameCheck.availableColor(the_color);
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
    }


}
