package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private static final HashSet<GameData> gameSet = new HashSet<GameData>(); // 存储AuthData

    @Override
    public void createGame() throws DataAccessException {

    }

    @Override
    public GameData getGame() throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame() throws DataAccessException {

    }

    public void clear()
    {
        gameSet.clear();
    }
}
