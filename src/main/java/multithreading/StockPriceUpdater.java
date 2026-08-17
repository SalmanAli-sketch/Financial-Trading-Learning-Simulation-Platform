package multithreading;

import dao.StockDAO;
import model.Stock;

import java.util.List;
import java.util.Random;

public class StockPriceUpdater implements Runnable
{
    private final StockDAO stockDAO;
    private final Random random;

    private volatile boolean running;

    public StockPriceUpdater()
    {
        stockDAO = new StockDAO();
        random = new Random();
        running = true;
    }


    // ============================================
    // RUN
    // ============================================

    @Override
    public void run()
    {
        System.out.println(
                "Stock Price Updater started."
        );

        while (running)
        {
            try
            {
                updateStockPrices();

                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();

                System.out.println(
                        "Stock Price Updater interrupted."
                );

                break;
            }
        }

        System.out.println(
                "Stock Price Updater stopped."
        );
    }


    // ============================================
    // UPDATE STOCK PRICES
    // ============================================

    private void updateStockPrices()
    {
        /*
         * For now we update a few known stocks.
         *
         * Later we can add a DAO method that returns
         * List<Stock> and update every stock.
         */

        updatePrice("TCS");
        updatePrice("INFY");
        updatePrice("RELIANCE");
        updatePrice("HDFCBANK");
        updatePrice("ITC");
    }


    // ============================================
    // UPDATE SINGLE STOCK
    // ============================================

    private void updatePrice(String symbol)
    {
        Stock stock =
                stockDAO.getStockBySymbol(symbol);

        if (stock == null)
        {
            return;
        }

        double currentPrice =
                stock.getCurrentPrice();

        /*
         * Random change between -5% and +5%
         */

        double change =
                (random.nextDouble() * 0.10) - 0.05;

        double newPrice =
                currentPrice + (currentPrice * change);

        /*
         * Prevent price from becoming zero/negative.
         */

        if (newPrice < 1)
        {
            newPrice = 1;
        }

        stockDAO.updatePrice(
                stock.getStockId(),
                newPrice
        );

        System.out.println(
                "[MARKET] "
                        + stock.getSymbol()
                        + " : ₹"
                        + String.format("%.2f", currentPrice)
                        + " → ₹"
                        + String.format("%.2f", newPrice)
        );
    }


    // ============================================
    // STOP THREAD
    // ============================================

    public void stop()
    {
        running = false;
    }
}