package dataAccess;

import model.GameData;

import java.util.List;

public class MemoryGame  implements GameDAO{

    private final DBoperation dBoperation = new DBoperation(); // create the object of DB operation


    @Override
    public int createGame(String game_name) {
        int the_game_id = dBoperation.get_new_game_id();
        dBoperation.create_game(the_game_id, null, null, game_name, null);
        return the_game_id;
    }

    @Override
    public GameData getGame(int gameID) throws IllegalAccessException {
        return dBoperation.getGame("gameID", new GameData(gameID, null, null, null,null));
    }

    @Override
    public List<GameData> listGames() {
        return (List<GameData>) dBoperation.show_all_games();
    }

    @Override
    public void updateGame(int gameID, String game) throws DataAccessException, IllegalAccessException {
       GameData updated = dBoperation.getGame("gameID", new GameData(gameID, null, null, null, null));
       if (updated == null) {throw new DataAccessException("Error: bad request");}
       else {
           dBoperation.delGame("all", updated);
           dBoperation.create_game(updated.gameID(), updated.whiteUsername(), updated.blackUsername(), updated.gameName(), game);
       }
    }

    @Override
    public void clear() {
        dBoperation.clear_game();
    }

    @Override
    public boolean game_exists(int game_id) throws IllegalAccessException {
        return getGame(game_id) != null;
    }

    @Override
    public void update_players(int gameID, String username, String the_color) throws IllegalAccessException, DataAccessException
    {
        GameData for_update = dBoperation.getGame("gameID", new GameData(gameID, null, null, null, null));
        if (for_update == null) {throw new DataAccessException("Error: bad request");}
        else
        {
            dBoperation.delGame("all", for_update);
            if (the_color.equals("WHITE"))
            {
                dBoperation.create_game(for_update.gameID(), username, for_update.blackUsername(), for_update.gameName(), for_update.game());
            }
            else if (the_color.equals("BLACK"))
            {
                dBoperation.create_game(for_update.gameID(), username, for_update.whiteUsername(), for_update.gameName(),for_update.game());
            }
            else
            {
                throw new IllegalAccessException("Error: invalid color.");
            }
        }

    }

    @Override
    public void join_game(int gameID, String username, String the_color) throws DataAccessException, IllegalAccessException {
        boolean color_good;
        try
        {
            color_good = color_free(the_color, gameID); // if got error
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Error: bad request");
        }
        if (color_good) // if color is available
        {
            update_players(gameID, username, the_color);
        }
        else
        {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public boolean color_free(String the_color, int gameID) throws DataAccessException, IllegalAccessException {
        if (game_exists(gameID))
        {
            GameData game_check = getGame(gameID);
            return game_check.available_color(the_color);
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
    }


}
