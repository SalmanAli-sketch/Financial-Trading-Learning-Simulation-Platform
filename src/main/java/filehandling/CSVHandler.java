package filehandling;

import dao.HoldingDAO;
import dao.PortfolioDAO;
import dao.StockDAO;
import model.Holding;
import model.Portfolio;
import model.Stock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVHandler
{
    private static final String BACKUP_DIRECTORY =
            "backup";

    private final PortfolioDAO portfolioDAO;
    private final HoldingDAO holdingDAO;
    private final StockDAO stockDAO;

    public CSVHandler()
    {
        portfolioDAO = new PortfolioDAO();
        holdingDAO = new HoldingDAO();
        stockDAO = new StockDAO();
    }


    // ============================================
    // EXPORT PORTFOLIO TO CSV
    // ============================================

    public void exportPortfolio(int userId)
    {
        createBackupDirectory();

        Portfolio portfolio =
                portfolioDAO.getPortfolioByUserId(userId);

        if (portfolio == null)
        {
            System.out.println(
                    "Portfolio not found."
            );

            return;
        }

        List<Holding> holdings =
                holdingDAO.getPortfolioHoldings(
                        portfolio.getPortfolioId()
                );

        String fileName =
                "backup/portfolio_user_"
                        + userId
                        + ".csv";

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(fileName)))
        {
            // CSV header

            writer.write(
                    "Stock,Quantity,Average Price,"
                            + "Current Price,Current Value"
            );

            writer.newLine();

            // CSV data

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

                    writer.write(
                            stock.getSymbol()
                                    + ","
                                    + holding.getQuantity()
                                    + ","
                                    + holding.getAveragePrice()
                                    + ","
                                    + stock.getCurrentPrice()
                                    + ","
                                    + currentValue
                    );

                    writer.newLine();
                }
            }

            System.out.println(
                    "Portfolio exported successfully."
            );
        }
        catch (IOException e)
        {
            System.out.println(
                    "Failed to export portfolio."
            );

            e.printStackTrace();
        }
    }


    // ============================================
    // CREATE BACKUP DIRECTORY
    // ============================================

    private void createBackupDirectory()
    {
        File directory =
                new File(BACKUP_DIRECTORY);

        if (!directory.exists())
        {
            directory.mkdirs();
        }
    }
}