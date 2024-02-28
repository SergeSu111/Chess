package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO {
    int createGame(String game_name) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException, IllegalAccessException;
    List<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID, String game) throws DataAccessException, IllegalAccessException;

    void clear();

    boolean game_exists(int game_id) throws DataAccessException, IllegalAccessException;

    void update_players(int gameID, String username, String the_color) throws IllegalAccessException, DataAccessException;

    void join_game(int gameID, String username, String the_color) throws DataAccessException, IllegalAccessException;

    boolean color_free(String the_color, int gameID) throws DataAccessException, IllegalAccessException;
}
