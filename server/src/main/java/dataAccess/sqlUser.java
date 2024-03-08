package dataAccess;


import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
        if (username != null || email != null || password != null)
        {
            try(var conn = DatabaseManager.getConnection())
            {
                try (var preparedStatement = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)"))
                {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String hash = encoder.encode(password);

                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, hash);

                    preparedStatement.executeUpdate();
                }
            }
            catch (SQLException E)
            {
                throw new DataAccessException(E.getMessage());
            }
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException, IllegalAccessException {
        UserData userData = null;
       try (var conn = DatabaseManager.getConnection())
       {
           // 最后两个参数意味数据库的改变不会影响结果集 并且结果集只是可读 不能修改数据库的信息
           try (var preparedStatement = conn.prepareStatement("SELECT username FROM Users WHERE username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
               preparedStatement.setString(1, username);

               var result = preparedStatement.executeQuery(); // 将数据库里select的数据给result
               int count = 0;
               if (result.last())
               {
                   count = result.getRow(); // 直接把result所有的行数拿来
               }
               if (count == 1) // 如果行数为1 整数数据库里只有这一个数据 那么直接拿过来
               {
                   // construct a new userDate reference , pass in these user information as parameters into the class
                   // 将拿来的数据的每一行 都给UserData.
                   userData = new UserData(result.getString(1), result.getString(2), result.getString(3));
               }
               else if (count > 1) // 证明数据库里有不止一个用户信息
               {
                    throw new DataAccessException("Error: There are more than one user data.");
               }
           }
       }
       catch (SQLException E)
       {
           throw new DataAccessException(E.getMessage());
       }
        return userData; // if userData == null 证明里面没有数据
    }


    @Override
    public boolean userIsStored(String username) throws DataAccessException, IllegalAccessException
    {
        boolean existed = false; // 如果没有用户在数据库 或者有多个 则直接返回
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM Users WHERE username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
            {
                preparedStatement.setString(1, username);
                var result = preparedStatement.executeQuery();
                int count = 0;
                if (result.last())
                {
                    count = result.getRow();
                }
                if (count == 1)
                {
                    existed = true;
                }
                else if (count > 1)
                {
                    throw new DataAccessException("Error: There are more than one user data.");
                }
            }
        }
        catch (SQLException E)
        {
            throw new DataAccessException(E.getMessage());
        }
        return  existed;
    }

    @Override
    public boolean passwordMatch(String testUsername, String password) throws DataAccessException, IllegalAccessException {
        String Password = null; // 之后设置这个testPassword 然后如果不是null 则返回true 如果是null 返回false
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password FROM Users WHERE username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                preparedStatement.setString(1, testUsername);
                var result = preparedStatement.executeQuery();
                int count = 0;
                if (result.last()) {
                    count = result.getRow();
                }
                if (count == 1) {
                    Password = result.getString(2);
                } else if (count > 1) {
                    throw new DataAccessException("Error: There are more than one user data.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (Password != null)
        {
            return true;
        }
        return false;


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
