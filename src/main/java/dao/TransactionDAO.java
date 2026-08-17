package dao;

import database.DatabaseConnection;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO
{
    // ============================================
    // CREATE TRANSACTION
    // ============================================

    public boolean createTransaction(
            Transaction transaction)
    {
        String sql = """
                INSERT INTO transactions
                (
                    user_id,
                    stock_id,
                    transaction_type,
                    quantity,
                    price,
                    total_amount
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            setTransactionValues(
                    statement,
                    transaction
            );

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // CREATE TRANSACTION
    // TRANSACTION CONNECTION
    // ============================================

    public boolean createTransaction(
            Connection connection,
            Transaction transaction)
            throws SQLException
    {
        String sql = """
                INSERT INTO transactions
                (
                    user_id,
                    stock_id,
                    transaction_type,
                    quantity,
                    price,
                    total_amount
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            setTransactionValues(
                    statement,
                    transaction
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ============================================
    // GET TRANSACTION BY ID
    // ============================================

    public Transaction getTransactionById(
            int transactionId)
    {
        String sql = """
                SELECT transaction_id,
                       user_id,
                       stock_id,
                       transaction_type,
                       quantity,
                       price,
                       total_amount,
                       transaction_time
                FROM transactions
                WHERE transaction_id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    transactionId
            );

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return mapTransaction(
                            resultSet
                    );
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
    // GET USER TRANSACTIONS
    // ============================================

    public List<Transaction> getUserTransactions(
            int userId)
    {
        String sql = """
                SELECT transaction_id,
                       user_id,
                       stock_id,
                       transaction_type,
                       quantity,
                       price,
                       total_amount,
                       transaction_time
                FROM transactions
                WHERE user_id = ?
                ORDER BY transaction_time DESC
                """;

        List<Transaction> transactions =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                while (resultSet.next())
                {
                    transactions.add(
                            mapTransaction(
                                    resultSet
                            )
                    );
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return transactions;
    }


    // ============================================
    // SET TRANSACTION VALUES
    // ============================================

    private void setTransactionValues(
            PreparedStatement statement,
            Transaction transaction)
            throws SQLException
    {
        statement.setInt(
                1,
                transaction.getUserId()
        );

        statement.setInt(
                2,
                transaction.getStockId()
        );

        statement.setString(
                3,
                transaction.getTransactionType()
        );

        statement.setInt(
                4,
                transaction.getQuantity()
        );

        statement.setDouble(
                5,
                transaction.getPrice()
        );

        statement.setDouble(
                6,
                transaction.getTotalAmount()
        );
    }


    // ============================================
    // MAP RESULT TO TRANSACTION
    // ============================================

    private Transaction mapTransaction(
            ResultSet resultSet)
            throws SQLException
    {
        return new Transaction(
                resultSet.getInt("transaction_id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("stock_id"),
                resultSet.getString("transaction_type"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("price"),
                resultSet.getDouble("total_amount"),
                resultSet.getTimestamp(
                        "transaction_time"
                ).toLocalDateTime()
        );
    }
}