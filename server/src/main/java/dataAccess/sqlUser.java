package dataAccess;


import model.UserData;

import java.sql.SQLException;
import java.util.Objects;

//  create User table
public class sqlUser implements UserDAO {

    public sqlUser() throws DataAccessException  // what is different between DataAccessException and ResponseException?
    {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        // drop table? when do i need to use try and when I dont need? what that means by try try try?
        try(var conn = DatabaseManager.getConnection())
        {
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Users"))
            {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void createUser(String username, String email, String password) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)"))
            {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);

                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException E)
        {
            throw new DataAccessException(E.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException, IllegalAccessException {
       try (var conn = DatabaseManager.getConnection())
       {
           try (var preparedStatement = conn.prepareStatement("SELECT username FROM Users WHERE username = ?")) {
               preparedStatement.setString(1, username);

               preparedStatement.executeUpdate();
           }
       }
       catch (SQLException E)
       {
           throw new DataAccessException(E.getMessage());
       }
    }

    @Override
    public boolean userIsStored(String username) throws DataAccessException, IllegalAccessException
    {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM Users WHERE username = ?"))
            {
                preparedStatement.setString(1, username);
                try (var result = preparedStatement.executeQuery())
                {
                    while (result.next())
                    {
                        if (Objects.equals(username, result.getString("username")))
                        {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        catch (SQLException E)
        {
            throw new DataAccessException(E.getMessage());
        }
    }

    @Override
    public boolean passwordMatch(String testUsername, String password) throws DataAccessException, IllegalAccessException {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password FROM Users WHERE username = ?, passowrd = ?"))
            {
                preparedStatement.setString(1, testUsername);
                preparedStatement.setString(1, password);
                try (var result = preparedStatement.executeQuery())
                {
                    while (result.next())
                    {
                        if (Objects.equals(testUsername, result.getString("username")) && Objects.equals(password, result.getString("password")))
                        {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {  // Why the type is String?
            """
            CREATE TABLE IF NOT EXISTS  Users (
              `username` varchar(255) NOT NULL,
              `password` varchar(255) NOT NULL,
              `email`varchar(255),
              PRIMARY KEY (`username`),
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
