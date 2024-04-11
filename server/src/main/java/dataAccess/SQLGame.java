package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;

public class SQLGame implements GameDAO{

    public SQLGame() throws DataAccessException {
        configureSqlDatabase();
    }
    @Override
    public int createGame(String gameName) throws DataAccessException {
        Random random = new Random();
        int randomPositiveInt = random.nextInt(Integer.MAX_VALUE - 1) + 1;
        String game = new Gson().toJson(new ChessGame());

        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Games (gameID, gameName, game) " + "VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
            {
                preparedStatement.setInt(1, randomPositiveInt);
                preparedStatement.setString(2, gameName);
                preparedStatement.setString(3, game);
                //preparedStatement.executeUpdate();

                System.out.println("Executing SQL: " + preparedStatement.toString());

                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected == 0)
                {
                    throw new DataAccessException("Error: Insertion failed, no row affected.");
                }
            }
        }
        catch (Exception e)
        {
            throw new DataAccessException("Error: bad request");
        }
        return randomPositiveInt;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = null;
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM Games WHERE gameID = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
            {
                preparedStatement.setInt(1, gameID);
                var result = preparedStatement.executeQuery();
                int count = 0;
                if (result.last())
                {
                    count = result.getRow();
                }
                if (count == 1)
                {
                    game = new GameData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                }
                else if (count == 0)
                {
                    throw new DataAccessException("Error: There is no game in DB");
                }
                else if (count > 1)
                {
                    throw new DataAccessException("Error: There are more than one game data.");
                }
            }
        }
        catch (SQLException E)
        {
            throw new DataAccessException(E.getMessage());
        }
        return game;
    }

    public void addGame(int gameID, String whiteUsername, String blackUsername, String gameName, String game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Games (gameID, whiteUsername, blackUsername, gameName, game) VALUES(?,?, ?, ?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
            {
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, whiteUsername);
                preparedStatement.setString(3, blackUsername);
                preparedStatement.setString(4, gameName);
                preparedStatement.setString(5, game);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM Games", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
            {
                var result = preparedStatement.executeQuery();
                while (result.next())
                {
                    games.add(new GameData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)));
                }
            }
        }

        catch (SQLException ex)
        {
            throw new DataAccessException(ex.getMessage());
        }
        return games;
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Games"))
            {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException E)
        {
            throw new DataAccessException(E.getMessage());
        }

    }

    @Override
    public boolean gameExists(int gameId) throws IllegalAccessException, DataAccessException {
        boolean game = false;
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID FROM Games WHERE gameID = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
            {
                preparedStatement.setInt(1, gameId);
                var result = preparedStatement.executeQuery();
                int count = 0;
                if (result.last())
                {
                    count = result.getRow();
                }
                if (count == 1)
                {
                    game = true;
                }
                else if (count > 1)
                {
                    throw new DataAccessException("Error: There are more than one game data.");
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException(ex.getMessage());
        }
        return game;
    }

    public void delGame(int gameID) throws DataAccessException, IllegalAccessException {
        if (!(gameExists(gameID)))
        {
            throw new DataAccessException("Error: your game is not existed");
        }
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Games WHERE gameID = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
            {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void updatePlayers(int gameID, String username, String theColor) throws IllegalAccessException, DataAccessException {
        GameData update = getGame(gameID);
        if (update != null)
        {
            delGame(update.gameID());
            if (theColor.equals("BLACK"))
            {
                addGame(update.gameID(), update.whiteUsername(), username, update.gameName(), update.game());
            }
            else if (theColor.equals("WHITE"))
            {
                addGame(update.gameID(),username, update.blackUsername(), update.gameName(), update.game());
            }
            else
            {
                throw new IllegalAccessException("Error: invalid color.");
            }
        }

    }



    @Override
    public void joinGame(int gameID, String username, String theColor) throws DataAccessException, IllegalAccessException
    {
        boolean colorIsEmpty;
        try
        {
            colorIsEmpty = colorFree(theColor, gameID);
        }
        catch (IllegalAccessException ex)
        {
            throw new DataAccessException("Error: bad request");
        }
        if (colorIsEmpty)
        {
            updatePlayers(gameID, username, theColor);
        }
        else
        {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public void updateGame(ChessGame updatedGame, int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET game=? WHERE gameID=?")) {
                var gameStringObj = new Gson().toJson(updatedGame);
                preparedStatement.setString(1, gameStringObj);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new DataAccessException("Error: bad request");
        }
    }


    @Override
    public boolean colorFree(String theColor, int gameID) throws DataAccessException, IllegalAccessException {
        if (gameExists(gameID))
        {
            GameData gameCheck = getGame(gameID);
            return gameCheck.availableColor(theColor);
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
    }



    private final String[] createStatements = {  // Why the type is String?
            """
            CREATE TABLE IF NOT EXISTS Games(
              `gameID` INT NOT NULL AUTO_INCREMENT, 
              `whiteUsername` varchar(255) DEFAULT NULL,
              `blackUsername` varchar(255) DEFAULT NULL,
              `gameName` varchar(255) NOT NULL,
              `game` JSON NOT NULL,
               PRIMARY KEY (`gameID`)
            );
            """
    };


    public void configureSqlDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) { // what is this createStatement?
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException( String.format("Unable to configure GAME database: %s", ex.getMessage()));
        }
    }
}
