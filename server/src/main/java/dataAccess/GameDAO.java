package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.List;

public interface GameDAO {
    int createGame(String game_name);
    GameData getGame(int gameID) throws IllegalAccessException;
    HashSet<GameData> listGames();

    void updateGame(int gameID, String game) throws DataAccessException, IllegalAccessException;

    void clear();

    boolean game_exists(int game_id) throws IllegalAccessException;

    void update_players(int gameID, String username, String the_color) throws IllegalAccessException, DataAccessException;

    void join_game(int gameID, String username, String the_color) throws DataAccessException, IllegalAccessException;

    boolean color_free(String the_color, int gameID) throws DataAccessException, IllegalAccessException;
}
