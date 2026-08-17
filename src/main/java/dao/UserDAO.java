package dao;

import database.DatabaseConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO
{
    // ============================================
    // CREATE USER
    // ============================================

    public boolean createUser(User user)
    {
        String sql = """
                INSERT INTO users
                (username, email, password, balance)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setDouble(4, user.getBalance());

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // GET USER BY ID
    // ============================================

    public User getUserById(int userId)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return getUserById(connection, userId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    // ============================================
    // GET USER BY ID
    // SAME TRANSACTION CONNECTION
    // ============================================

    public User getUserById(
            Connection connection,
            int userId) throws SQLException
    {
        String sql = """
                SELECT user_id,
                       username,
                       email,
                       password,
                       balance
                FROM users
                WHERE user_id = ?
                FOR UPDATE
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }


    // ============================================
    // GET USER BY USERNAME
    // ============================================

    public User getUserByUsername(String username)
    {
        String sql = """
                SELECT user_id,
                       username,
                       email,
                       password,
                       balance
                FROM users
                WHERE username = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return mapUser(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    // ============================================
    // UPDATE BALANCE
    // ============================================

    public boolean updateBalance(
            int userId,
            double newBalance)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return updateBalance(
                    connection,
                    userId,
                    newBalance
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // UPDATE BALANCE
    // SAME TRANSACTION CONNECTION
    // ============================================

    public boolean updateBalance(
            Connection connection,
            int userId,
            double newBalance) throws SQLException
    {
        String sql = """
                UPDATE users
                SET balance = ?
                WHERE user_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setDouble(1, newBalance);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        }
    }


    // ============================================
    // GET ALL USERS
    // ============================================

    public void getAllUsers()
    {
        String sql = """
                SELECT user_id,
                       username,
                       email,
                       balance
                FROM users
                ORDER BY user_id
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery())
        {
            while (resultSet.next())
            {
                System.out.println(
                        "ID: "
                                + resultSet.getInt("user_id")
                                + ", Username: "
                                + resultSet.getString("username")
                                + ", Email: "
                                + resultSet.getString("email")
                                + ", Balance: "
                                + resultSet.getDouble("balance")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    // ============================================
    // MAP RESULT TO USER
    // ============================================

    private User mapUser(
            ResultSet resultSet) throws SQLException
    {
        return new User(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getDouble("balance")
        );
    }
}