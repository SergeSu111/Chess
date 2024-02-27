package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public class MemoryGame  implements GameDAO{

    private DBoperation dBoperation = new DBoperation(); // create the object of DB operation


    @Override
    public int createGame(ChessGame game) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
       //
    }

    @Override
    public void clear() {
        dBoperation.clear_game();
    }
}
