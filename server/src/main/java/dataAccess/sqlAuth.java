package dataAccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class sqlAuth  implements AuthDAO{

    public sqlAuth() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException
    {
        try(var conn = DatabaseManager.getConnection())
        {
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Auths"))
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
    public String createAuth(String username) throws DataAccessException {
        String newAuthToken = UUID.randomUUID().toString(); // get a random token
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement( "INSERT INTO Auths (authToken, username) VALUES(?, ?)"))
        {
            preparedStatement.setString(1,newAuthToken); //设置当前的参数 把用户创建的token放进去
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate(); // 提交给数据库
        }
        catch (SQLException ex)
        {
            throw new DataAccessException(ex.getMessage());
        }
        return newAuthToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException, IllegalAccessException {
        AuthData auth = null; // 先设置为null 后面如果在数据库找到则更新 或有任何问题则还是null.

        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT authToken, username FROM Auths WHERE authToken = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
        {
            preparedStatement.setString(1, authToken);
            var result = preparedStatement.executeQuery(); // 将选择来的信息存储到result里
            int count = 0;
            if (result.last())
            {
                count = result.getRow();
            }
            if (count == 1)
            {
                auth = new AuthData(result.getString(1), result.getString(2)); // 则将result里的行都拿过来给AuthData
            }
            else if ( count > 1)
            {
                throw new DataAccessException("Error: There are more than one auth data.");
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        return auth;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, IllegalAccessException
    {
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("DELETE FROM Auths WHERE authToken = ?"))
        {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean authIsStored(String authToken) throws DataAccessException, IllegalAccessException {
        boolean existed = false;
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT username FROM Auths WHERE authToken = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
        {
            preparedStatement.setString(1, authToken);
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
                throw new DataAccessException("Error: There are more than one auth data.");
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        return existed;
    }

    @Override
    public String getUserName(String authToken) throws IllegalAccessException, DataAccessException {
        // 先设置要得到的username 为null
        // 然后连接数据库查询 如果数据库有则将username 修改为其值 否则直接返回为null 有问题throw error
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT username FROM Auths WHERE authToken = ?",  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
        {
            preparedStatement.setString(1, authToken);
            try (var result = preparedStatement.executeQuery())
            {
                if (!result.next())
                {
                    throw new DataAccessException("Error: Username not exist");
                }
                String myUsername = result.getString("username");
                return myUsername;
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        catch (DataAccessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Error: bad request");
        }
    }


    private final String[] createStatements = {  // Why the type is String?
            """
            CREATE TABLE IF NOT EXISTS  Auths(
              `authToken` varchar(255) NOT NULL,
              `username` varchar(255) NOT NULL,
              `email`varchar(255),
              PRIMARY KEY (`authToken`)
            );
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
