package dao;

import database.DatabaseConnection;
import model.Holding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoldingDAO
{
    // ============================================
    // CREATE HOLDING
    // ============================================

    public boolean createHolding(Holding holding)
    {
        String sql = """
                INSERT INTO holdings
                (portfolio_id, stock_id, quantity, average_price)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    holding.getPortfolioId()
            );

            statement.setInt(
                    2,
                    holding.getStockId()
            );

            statement.setInt(
                    3,
                    holding.getQuantity()
            );

            statement.setDouble(
                    4,
                    holding.getAveragePrice()
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
    // CREATE HOLDING
    // SAME TRANSACTION CONNECTION
    // ============================================

    public boolean createHolding(
            Connection connection,
            Holding holding)
            throws SQLException
    {
        String sql = """
                INSERT INTO holdings
                (portfolio_id, stock_id, quantity, average_price)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    holding.getPortfolioId()
            );

            statement.setInt(
                    2,
                    holding.getStockId()
            );

            statement.setInt(
                    3,
                    holding.getQuantity()
            );

            statement.setDouble(
                    4,
                    holding.getAveragePrice()
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ============================================
    // GET HOLDING
    // ============================================

    public Holding getHolding(
            int portfolioId,
            int stockId)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return getHolding(
                    connection,
                    portfolioId,
                    stockId
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    // ============================================
    // GET HOLDING
    // SAME TRANSACTION CONNECTION
    // ============================================

    public Holding getHolding(
            Connection connection,
            int portfolioId,
            int stockId)
            throws SQLException
    {
        String sql = """
                SELECT holding_id,
                       portfolio_id,
                       stock_id,
                       quantity,
                       average_price
                FROM holdings
                WHERE portfolio_id = ?
                AND stock_id = ?
                FOR UPDATE
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    portfolioId
            );

            statement.setInt(
                    2,
                    stockId
            );

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return mapHolding(resultSet);
                }
            }
        }

        return null;
    }


    // ============================================
    // GET PORTFOLIO HOLDINGS
    // ============================================

    public List<Holding> getPortfolioHoldings(
            int portfolioId)
    {
        String sql = """
                SELECT holding_id,
                       portfolio_id,
                       stock_id,
                       quantity,
                       average_price
                FROM holdings
                WHERE portfolio_id = ?
                """;

        List<Holding> holdings =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    portfolioId
            );

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                while (resultSet.next())
                {
                    holdings.add(
                            mapHolding(resultSet)
                    );
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return holdings;
    }


    // ============================================
    // UPDATE HOLDING
    // ============================================

    public boolean updateHolding(
            int holdingId,
            int quantity,
            double averagePrice)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return updateHolding(
                    connection,
                    holdingId,
                    quantity,
                    averagePrice
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // UPDATE HOLDING
    // SAME TRANSACTION CONNECTION
    // ============================================

    public boolean updateHolding(
            Connection connection,
            int holdingId,
            int quantity,
            double averagePrice)
            throws SQLException
    {
        String sql = """
                UPDATE holdings
                SET quantity = ?,
                    average_price = ?
                WHERE holding_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    quantity
            );

            statement.setDouble(
                    2,
                    averagePrice
            );

            statement.setInt(
                    3,
                    holdingId
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ============================================
    // DELETE HOLDING
    // ============================================

    public boolean deleteHolding(
            int holdingId)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return deleteHolding(
                    connection,
                    holdingId
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // DELETE HOLDING
    // SAME TRANSACTION CONNECTION
    // ============================================

    public boolean deleteHolding(
            Connection connection,
            int holdingId)
            throws SQLException
    {
        String sql = """
                DELETE FROM holdings
                WHERE holding_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(
                    1,
                    holdingId
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ============================================
    // MAP RESULT TO HOLDING
    // ============================================

    private Holding mapHolding(
            ResultSet resultSet)
            throws SQLException
    {
        return new Holding(
                resultSet.getInt("holding_id"),
                resultSet.getInt("portfolio_id"),
                resultSet.getInt("stock_id"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("average_price")
        );
    }
}