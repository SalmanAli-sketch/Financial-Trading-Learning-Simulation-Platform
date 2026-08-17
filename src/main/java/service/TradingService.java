package service;

import dao.HoldingDAO;
import dao.PortfolioDAO;
import dao.StockDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import database.DatabaseConnection;
import model.Holding;
import model.Portfolio;
import model.Stock;
import model.Transaction;
import model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class TradingService
{
    private final UserDAO userDAO;
    private final StockDAO stockDAO;
    private final PortfolioDAO portfolioDAO;
    private final HoldingDAO holdingDAO;
    private final TransactionDAO transactionDAO;

    /*
     * Fair ReentrantLock.
     *
     * Only one trading operation can modify
     * trading data at a time.
     */
    private final ReentrantLock tradingLock;


    // ============================================
    // CONSTRUCTOR
    // ============================================

    public TradingService()
    {
        userDAO = new UserDAO();
        stockDAO = new StockDAO();
        portfolioDAO = new PortfolioDAO();
        holdingDAO = new HoldingDAO();
        transactionDAO = new TransactionDAO();

        tradingLock =
                new ReentrantLock(true);
    }


    // ============================================
    // BUY STOCK
    // ============================================

    public boolean buyStock(
            int userId,
            int stockId,
            int quantity)
    {
        if (quantity <= 0)
        {
            System.out.println(
                    "Quantity must be greater than zero."
            );

            return false;
        }

        tradingLock.lock();

        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            /*
             * Start JDBC transaction.
             */

            connection.setAutoCommit(false);

            try
            {
                // --------------------------------
                // GET USER + LOCK USER ROW
                // --------------------------------

                User user =
                        userDAO.getUserById(
                                connection,
                                userId
                        );

                if (user == null)
                {
                    throw new SQLException(
                            "User not found."
                    );
                }

                // --------------------------------
                // GET STOCK + LOCK STOCK ROW
                // --------------------------------

                Stock stock =
                        stockDAO.getStockById(
                                connection,
                                stockId
                        );

                if (stock == null)
                {
                    throw new SQLException(
                            "Stock not found."
                    );
                }

                // --------------------------------
                // CHECK STOCK
                // --------------------------------

                if (stock.getAvailableQuantity()
                        < quantity)
                {
                    throw new SQLException(
                            "Not enough shares available."
                    );
                }

                // --------------------------------
                // CALCULATE TOTAL
                // --------------------------------

                double totalAmount =
                        stock.getCurrentPrice()
                                * quantity;

                // --------------------------------
                // CHECK BALANCE
                // --------------------------------

                if (user.getBalance()
                        < totalAmount)
                {
                    throw new SQLException(
                            "Insufficient balance."
                    );
                }

                // --------------------------------
                // GET PORTFOLIO
                // --------------------------------

                Portfolio portfolio =
                        portfolioDAO
                                .getPortfolioByUserId(
                                        userId
                                );

                if (portfolio == null)
                {
                    throw new SQLException(
                            "Portfolio not found."
                    );
                }

                // --------------------------------
                // GET HOLDING + LOCK ROW
                // --------------------------------

                Holding holding =
                        holdingDAO.getHolding(
                                connection,
                                portfolio.getPortfolioId(),
                                stockId
                        );

                // --------------------------------
                // UPDATE USER BALANCE
                // --------------------------------

                double newBalance =
                        user.getBalance()
                                - totalAmount;

                if (!userDAO.updateBalance(
                        connection,
                        userId,
                        newBalance))
                {
                    throw new SQLException(
                            "Balance update failed."
                    );
                }

                // --------------------------------
                // UPDATE STOCK QUANTITY
                // --------------------------------

                int newStockQuantity =
                        stock.getAvailableQuantity()
                                - quantity;

                if (!stockDAO
                        .updateAvailableQuantity(
                                connection,
                                stockId,
                                newStockQuantity))
                {
                    throw new SQLException(
                            "Stock quantity update failed."
                    );
                }

                // --------------------------------
                // CREATE / UPDATE HOLDING
                // --------------------------------

                if (holding == null)
                {
                    Holding newHolding =
                            new Holding(
                                    portfolio.getPortfolioId(),
                                    stockId,
                                    quantity,
                                    stock.getCurrentPrice()
                            );

                    if (!holdingDAO.createHolding(
                            connection,
                            newHolding))
                    {
                        throw new SQLException(
                                "Holding creation failed."
                        );
                    }
                }
                else
                {
                    int oldQuantity =
                            holding.getQuantity();

                    double oldAveragePrice =
                            holding.getAveragePrice();

                    int newQuantity =
                            oldQuantity + quantity;

                    double newAveragePrice =
                            (
                                    (oldQuantity
                                            * oldAveragePrice)
                                            +
                                            (quantity
                                                    * stock.getCurrentPrice())
                            ) / newQuantity;

                    if (!holdingDAO.updateHolding(
                            connection,
                            holding.getHoldingId(),
                            newQuantity,
                            newAveragePrice))
                    {
                        throw new SQLException(
                                "Holding update failed."
                        );
                    }
                }

                // --------------------------------
                // CREATE TRANSACTION RECORD
                // --------------------------------

                Transaction transaction =
                        new Transaction(
                                userId,
                                stockId,
                                "BUY",
                                quantity,
                                stock.getCurrentPrice(),
                                totalAmount
                        );

                if (!transactionDAO
                        .createTransaction(
                                connection,
                                transaction))
                {
                    throw new SQLException(
                            "Transaction creation failed."
                    );
                }

                // --------------------------------
                // COMMIT
                // --------------------------------

                connection.commit();

                System.out.println(
                        "BUY successful: "
                                + quantity
                                + " shares of "
                                + stock.getSymbol()
                );

                return true;
            }
            catch (Exception e)
            {
                // --------------------------------
                // ROLLBACK
                // --------------------------------

                connection.rollback();

                System.out.println(
                        "BUY failed."
                );

                System.out.println(
                        "Transaction rolled back."
                );

                System.out.println(
                        "Reason: "
                                + e.getMessage()
                );

                return false;
            }
            finally
            {
                connection.setAutoCommit(true);
            }
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Database transaction error."
            );

            e.printStackTrace();

            return false;
        }
        finally
        {
            tradingLock.unlock();
        }
    }


    // ============================================
    // SELL STOCK
    // ============================================

    public boolean sellStock(
            int userId,
            int stockId,
            int quantity)
    {
        if (quantity <= 0)
        {
            System.out.println(
                    "Quantity must be greater than zero."
            );

            return false;
        }

        tradingLock.lock();

        try (Connection connection =
                     DatabaseConnection.getConnection())
        {
            /*
             * Start JDBC transaction.
             */

            connection.setAutoCommit(false);

            try
            {
                // --------------------------------
                // GET USER + LOCK USER ROW
                // --------------------------------

                User user =
                        userDAO.getUserById(
                                connection,
                                userId
                        );

                if (user == null)
                {
                    throw new SQLException(
                            "User not found."
                    );
                }

                // --------------------------------
                // GET STOCK + LOCK STOCK ROW
                // --------------------------------

                Stock stock =
                        stockDAO.getStockById(
                                connection,
                                stockId
                        );

                if (stock == null)
                {
                    throw new SQLException(
                            "Stock not found."
                    );
                }

                // --------------------------------
                // GET PORTFOLIO
                // --------------------------------

                Portfolio portfolio =
                        portfolioDAO
                                .getPortfolioByUserId(
                                        userId
                                );

                if (portfolio == null)
                {
                    throw new SQLException(
                            "Portfolio not found."
                    );
                }

                // --------------------------------
                // GET HOLDING + LOCK ROW
                // --------------------------------

                Holding holding =
                        holdingDAO.getHolding(
                                connection,
                                portfolio.getPortfolioId(),
                                stockId
                        );

                if (holding == null)
                {
                    throw new SQLException(
                            "You do not own this stock."
                    );
                }

                // --------------------------------
                // CHECK SHARES
                // --------------------------------

                if (holding.getQuantity()
                        < quantity)
                {
                    throw new SQLException(
                            "Insufficient shares."
                    );
                }

                // --------------------------------
                // CALCULATE TOTAL
                // --------------------------------

                double totalAmount =
                        stock.getCurrentPrice()
                                * quantity;

                // --------------------------------
                // UPDATE BALANCE
                // --------------------------------

                double newBalance =
                        user.getBalance()
                                + totalAmount;

                if (!userDAO.updateBalance(
                        connection,
                        userId,
                        newBalance))
                {
                    throw new SQLException(
                            "Balance update failed."
                    );
                }

                // --------------------------------
                // UPDATE HOLDING
                // --------------------------------

                int remainingQuantity =
                        holding.getQuantity()
                                - quantity;

                if (remainingQuantity == 0)
                {
                    if (!holdingDAO.deleteHolding(
                            connection,
                            holding.getHoldingId()))
                    {
                        throw new SQLException(
                                "Holding deletion failed."
                        );
                    }
                }
                else
                {
                    if (!holdingDAO.updateHolding(
                            connection,
                            holding.getHoldingId(),
                            remainingQuantity,
                            holding.getAveragePrice()))
                    {
                        throw new SQLException(
                                "Holding update failed."
                        );
                    }
                }

                // --------------------------------
                // RETURN SHARES TO MARKET
                // --------------------------------

                int newStockQuantity =
                        stock.getAvailableQuantity()
                                + quantity;

                if (!stockDAO
                        .updateAvailableQuantity(
                                connection,
                                stockId,
                                newStockQuantity))
                {
                    throw new SQLException(
                            "Stock quantity update failed."
                    );
                }

                // --------------------------------
                // CREATE TRANSACTION RECORD
                // --------------------------------

                Transaction transaction =
                        new Transaction(
                                userId,
                                stockId,
                                "SELL",
                                quantity,
                                stock.getCurrentPrice(),
                                totalAmount
                        );

                if (!transactionDAO
                        .createTransaction(
                                connection,
                                transaction))
                {
                    throw new SQLException(
                            "Transaction creation failed."
                    );
                }

                // --------------------------------
                // COMMIT
                // --------------------------------

                connection.commit();

                System.out.println(
                        "SELL successful: "
                                + quantity
                                + " shares of "
                                + stock.getSymbol()
                );

                return true;
            }
            catch (Exception e)
            {
                // --------------------------------
                // ROLLBACK
                // --------------------------------

                connection.rollback();

                System.out.println(
                        "SELL failed."
                );

                System.out.println(
                        "Transaction rolled back."
                );

                System.out.println(
                        "Reason: "
                                + e.getMessage()
                );

                return false;
            }
            finally
            {
                connection.setAutoCommit(true);
            }
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Database transaction error."
            );

            e.printStackTrace();

            return false;
        }
        finally
        {
            tradingLock.unlock();
        }
    }
}