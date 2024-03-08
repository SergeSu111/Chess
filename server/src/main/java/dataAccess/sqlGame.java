package dataAccess;

import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;

public class sqlGame implements GameDAO{
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws IllegalAccessException {
        return null;
    }

    @Override
    public HashSet<GameData> listGames() {
        return null;
    }

    @Override
    public void clear() {
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("TRUNCATE TABLE "))
        {

        }

    }

    @Override
    public boolean gameExists(int gameId) throws IllegalAccessException {
        return false;
    }

    @Override
    public void updatePlayers(int gameID, String username, String theColor) throws IllegalAccessException, DataAccessException {

    }

    @Override
    public void joinGame(int gameID, String username, String theColor) throws DataAccessException, IllegalAccessException {

    }

    @Override
    public boolean colorFree(String theColor, int gameID) throws DataAccessException, IllegalAccessException {
        return false;
    }

    private final String[] createStatements = {  // Why the type is String?
            """
            CREATE TABLE IF NOT EXISTS Games(
              `gameID` INT NOT NULL AUTO_INCREMENT, 
              `whiteUsername` varchar(255) DEFAULT NULL,
              `blackUsername` varchar(255) DEFAULT NULL,
              `gameName` varchar(255) NOT NULL,
              `game` JSON NOT NULL,
               PRIMARY KEY (`gameID`),
            )
            """
    };


    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) { // what is this createStatement?
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException( String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
