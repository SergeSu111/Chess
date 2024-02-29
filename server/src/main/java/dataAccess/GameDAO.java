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

    boolean gameExists(int game_id) throws IllegalAccessException;

    void updatePlayers(int gameID, String username, String the_color) throws IllegalAccessException, DataAccessException;

    void joinGame(int gameID, String username, String the_color) throws DataAccessException, IllegalAccessException;

    boolean colorFree(String the_color, int gameID) throws DataAccessException, IllegalAccessException;
}
