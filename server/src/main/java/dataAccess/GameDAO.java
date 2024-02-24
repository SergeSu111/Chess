package dataAccess;

public interface GameDAO {
    public void createGame() throws DataAccessException;
    public void getGame() throws DataAccessException;
    public void listGames() throws DataAccessException;

    public void updateGame() throws DataAccessException;

}
