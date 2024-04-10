package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.List;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws IllegalAccessException, DataAccessException;
    HashSet<GameData> listGames() throws DataAccessException;

    // void updateGame(int gameID, String game) throws DataAccessException, IllegalAccessException;

    void clear() throws DataAccessException;

    boolean gameExists(int gameId) throws IllegalAccessException, DataAccessException;

    void updatePlayers(int gameID, String username, String theColor) throws IllegalAccessException, DataAccessException;

    void joinGame(int gameID, String username, String theColor) throws DataAccessException, IllegalAccessException;

    void updateGame(ChessGame updatedGame, int gameID) throws DataAccessException;

    boolean colorFree(String theColor, int gameID) throws DataAccessException, IllegalAccessException;
}
