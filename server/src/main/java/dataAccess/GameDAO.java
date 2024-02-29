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

    boolean gameExists(int gameId) throws IllegalAccessException;

    void updatePlayers(int gameID, String username, String theColor) throws IllegalAccessException, DataAccessException;

    void joinGame(int gameID, String username, String theColor) throws DataAccessException, IllegalAccessException;

    boolean colorFree(String theColor, int gameID) throws DataAccessException, IllegalAccessException;
}
