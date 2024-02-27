package dataAccess;

import model.GameData;

import java.util.List;

public class MemoryGame  implements GameDAO{

    private DBoperation dBoperation = new DBoperation(); // create the object of DB operation


    @Override
    public int createGame(String game_name) throws DataAccessException {
        int the_game_id = dBoperation.get_new_game_id();
        dBoperation.create_game(the_game_id, null, null, game_name, null);
        return the_game_id;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException, IllegalAccessException {
        return dBoperation.getGame("gameID", new GameData(gameID, null, null, null,null));
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return (List<GameData>) dBoperation.show_all_games();
    }

    @Override
    public void updateGame(int gameID, String game) throws DataAccessException, IllegalAccessException {
       GameData updated = dBoperation.getGame("gameID", new GameData(gameID, null, null, null, null));
       dBoperation.delGame("all", updated);
       dBoperation.create_game(updated.gameID(),updated.whiteUsername(), updated.blackUsername(), updated.gameName(), game);
    }

    @Override
    public void clear() {
        dBoperation.clear_game();
    }

    @Override
    public boolean game_exists(int game_id) throws DataAccessException, IllegalAccessException {
        return getGame(game_id) != null;
    }


}
