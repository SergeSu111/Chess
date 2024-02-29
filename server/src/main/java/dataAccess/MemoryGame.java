package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MemoryGame  implements GameDAO{

    private final DBoperation dBoperation = new DBoperation(); // create the object of DB operation


    @Override
    public int createGame(String gameName) {
        int theGameId = dBoperation.getNewGameId();
        dBoperation.createGame(theGameId, null, null, gameName, null);
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
    public void clear() {
        dBoperation.clearGame();
    }

    @Override
    public boolean gameExists(int gameId) throws IllegalAccessException {
        return getGame(gameId) != null;
    }

    @Override
    public void updatePlayers(int gameID, String username, String theColor) throws IllegalAccessException, DataAccessException
    {
        GameData forUpdate = dBoperation.getGame("gameID", new GameData(gameID, null, null, null, null));
        if (forUpdate == null) {throw new DataAccessException("Error: bad request");}
        else
        {
            dBoperation.delGame("all", forUpdate);
            if (theColor.equals("WHITE"))
            {
                dBoperation.createGame(forUpdate.gameID(), username, forUpdate.blackUsername(), forUpdate.gameName(), forUpdate.game());
            }
            else if (theColor.equals("BLACK"))
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
    public void joinGame(int gameID, String username, String theColor) throws DataAccessException, IllegalAccessException {
        boolean colorGood;
        try
        {
            colorGood = colorFree(theColor, gameID); // if got error
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Error: bad request");
        }
        if (colorGood) // if color is available
        {
            updatePlayers(gameID, username, theColor);
        }
        else
        {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public boolean colorFree(String theColor, int gameID) throws DataAccessException, IllegalAccessException {
        if (gameExists(gameID))
        {
            GameData gameCheck = getGame(gameID);
            return gameCheck.availableColor(theColor);
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
    }


}
