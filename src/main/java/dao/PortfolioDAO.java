package dao;

import database.DatabaseConnection;
import model.Portfolio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PortfolioDAO
{
    // ============================================
    // CREATE PORTFOLIO
    // ============================================

    public boolean createPortfolio(Portfolio portfolio)
    {
        String sql = """
                INSERT INTO portfolios (user_id)
                VALUES (?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, portfolio.getUserId());

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================
    // GET PORTFOLIO BY ID
    // ============================================

    public Portfolio getPortfolioById(int portfolioId)
    {
        String sql = """
                SELECT portfolio_id, user_id
                FROM portfolios
                WHERE portfolio_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, portfolioId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return new Portfolio(
                        resultSet.getInt("portfolio_id"),
                        resultSet.getInt("user_id")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    // ============================================
    // GET PORTFOLIO BY USER ID
    // ============================================

    public Portfolio getPortfolioByUserId(int userId)
    {
        String sql = """
                SELECT portfolio_id, user_id
                FROM portfolios
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return new Portfolio(
                        resultSet.getInt("portfolio_id"),
                        resultSet.getInt("user_id")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}