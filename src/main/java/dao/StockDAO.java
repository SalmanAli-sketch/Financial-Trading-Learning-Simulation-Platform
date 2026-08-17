package dao;

import database.DatabaseConnection;
import model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDAO
{
    // ============================================
    // CREATE STOCK
    // ============================================

    public boolean createStock(Stock stock)
    {
        String sql = """
                INSERT INTO stocks
                (symbol, company_name,
                 current_price, available_quantity)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setString(1, stock.getSymbol());
            statement.setString(2, stock.getCompanyName());
            statement.setDouble(3, stock.getCurrentPrice());
            statement.setInt(4, stock.getAvailableQuantity());

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // GET STOCK BY ID
    // ============================================

    public Stock getStockById(int stockId)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return getStockById(
                    connection,
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
    // GET STOCK BY ID
    // TRANSACTION CONNECTION
    // ============================================

    public Stock getStockById(
            Connection connection,
            int stockId) throws SQLException
    {
        String sql = """
                SELECT stock_id,
                       symbol,
                       company_name,
                       current_price,
                       available_quantity
                FROM stocks
                WHERE stock_id = ?
                FOR UPDATE
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(1, stockId);

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return mapStock(resultSet);
                }
            }
        }

        return null;
    }


    // ============================================
    // GET STOCK BY SYMBOL
    // ============================================

    public Stock getStockBySymbol(String symbol)
    {
        String sql = """
                SELECT stock_id,
                       symbol,
                       company_name,
                       current_price,
                       available_quantity
                FROM stocks
                WHERE symbol = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setString(1, symbol);

            try (ResultSet resultSet =
                         statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return mapStock(resultSet);
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
    // GET ALL STOCKS
    // ============================================

    public List<Stock> getAllStocks()
    {
        String sql = """
                SELECT stock_id,
                       symbol,
                       company_name,
                       current_price,
                       available_quantity
                FROM stocks
                ORDER BY stock_id
                """;

        List<Stock> stocks =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery())
        {
            while (resultSet.next())
            {
                stocks.add(
                        mapStock(resultSet)
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return stocks;
    }


    // ============================================
    // UPDATE PRICE
    // ============================================

    public boolean updatePrice(
            int stockId,
            double newPrice)
    {
        String sql = """
                UPDATE stocks
                SET current_price = ?
                WHERE stock_id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setDouble(1, newPrice);
            statement.setInt(2, stockId);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // UPDATE AVAILABLE QUANTITY
    // ============================================

    public boolean updateAvailableQuantity(
            int stockId,
            int quantity)
    {
        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            return updateAvailableQuantity(
                    connection,
                    stockId,
                    quantity
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // UPDATE AVAILABLE QUANTITY
    // TRANSACTION CONNECTION
    // ============================================

    public boolean updateAvailableQuantity(
            Connection connection,
            int stockId,
            int quantity)
            throws SQLException
    {
        String sql = """
                UPDATE stocks
                SET available_quantity = ?
                WHERE stock_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql))
        {
            statement.setInt(1, quantity);
            statement.setInt(2, stockId);

            return statement.executeUpdate() > 0;
        }
    }


    // ============================================
    // MAP RESULT TO STOCK
    // ============================================

    private Stock mapStock(
            ResultSet resultSet)
            throws SQLException
    {
        return new Stock(
                resultSet.getInt("stock_id"),
                resultSet.getString("symbol"),
                resultSet.getString("company_name"),
                resultSet.getDouble("current_price"),
                resultSet.getInt("available_quantity")
        );
    }
}