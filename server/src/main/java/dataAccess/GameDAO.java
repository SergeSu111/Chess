package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO {
    int createGame(ChessGame game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    boolean updateGame(int gameID, ChessGame game) throws DataAccessException;  // game参数是ChessGame吗?

    boolean clear();
}
