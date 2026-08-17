package service;

import dao.HoldingDAO;
import dao.PortfolioDAO;
import dao.StockDAO;
import model.Holding;
import model.Portfolio;
import model.Stock;

import java.util.List;

public class PortfolioService
{
    private final PortfolioDAO portfolioDAO;
    private final HoldingDAO holdingDAO;
    private final StockDAO stockDAO;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public PortfolioService()
    {
        portfolioDAO = new PortfolioDAO();
        holdingDAO = new HoldingDAO();
        stockDAO = new StockDAO();
    }


    // ============================================
    // GET USER PORTFOLIO
    // ============================================

    public Portfolio getPortfolio(int userId)
    {
        return portfolioDAO.getPortfolioByUserId(userId);
    }


    // ============================================
    // GET USER HOLDINGS
    // ============================================

    public List<Holding> getHoldings(int userId)
    {
        Portfolio portfolio =
                portfolioDAO.getPortfolioByUserId(userId);

        if (portfolio == null)
        {
            return List.of();
        }

        return holdingDAO.getPortfolioHoldings(
                portfolio.getPortfolioId()
        );
    }


    // ============================================
    // DISPLAY HOLDINGS
    // ============================================

    public void displayHoldings(int userId)
    {
        List<Holding> holdings =
                getHoldings(userId);

        if (holdings.isEmpty())
        {
            System.out.println("No holdings found.");
            return;
        }

        System.out.println();
        System.out.println("========== YOUR HOLDINGS ==========");

        for (Holding holding : holdings)
        {
            Stock stock =
                    stockDAO.getStockById(
                            holding.getStockId()
                    );

            if (stock != null)
            {
                double currentValue =
                        holding.getQuantity()
                                * stock.getCurrentPrice();

                System.out.println(
                        "Stock: " + stock.getSymbol()
                                + " | Quantity: "
                                + holding.getQuantity()
                                + " | Average Price: ₹"
                                + holding.getAveragePrice()
                                + " | Current Price: ₹"
                                + stock.getCurrentPrice()
                                + " | Current Value: ₹"
                                + currentValue
                );
            }
        }

        System.out.println("===================================");
    }


    // ============================================
    // CALCULATE PORTFOLIO VALUE
    // ============================================

    public double calculatePortfolioValue(int userId)
    {
        List<Holding> holdings =
                getHoldings(userId);

        double totalValue = 0.0;

        for (Holding holding : holdings)
        {
            Stock stock =
                    stockDAO.getStockById(
                            holding.getStockId()
                    );

            if (stock != null)
            {
                totalValue +=
                        holding.getQuantity()
                                * stock.getCurrentPrice();
            }
        }

        return totalValue;
    }


    // ============================================
    // CALCULATE INVESTED AMOUNT
    // ============================================

    public double calculateInvestedAmount(int userId)
    {
        List<Holding> holdings =
                getHoldings(userId);

        double investedAmount = 0.0;

        for (Holding holding : holdings)
        {
            investedAmount +=
                    holding.getQuantity()
                            * holding.getAveragePrice();
        }

        return investedAmount;
    }


    // ============================================
    // CALCULATE PROFIT / LOSS
    // ============================================

    public double calculateProfitLoss(int userId)
    {
        double currentValue =
                calculatePortfolioValue(userId);

        double investedAmount =
                calculateInvestedAmount(userId);

        return currentValue - investedAmount;
    }
}