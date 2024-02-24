package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class MemoryGameDAO {

    private static final HashSet<GameData> gameSet = new HashSet<GameData>(); // 存储AuthData
    public void clear()
    {
        gameSet.clear();
    }
}
