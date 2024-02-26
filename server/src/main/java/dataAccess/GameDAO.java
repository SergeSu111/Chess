package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    public void createGame() throws DataAccessException;
    public GameData getGame() throws DataAccessException;
    public List<GameData> listGames() throws DataAccessException;

    public void updateGame() throws DataAccessException;

    public void clear() throws DataAccessException;
}
