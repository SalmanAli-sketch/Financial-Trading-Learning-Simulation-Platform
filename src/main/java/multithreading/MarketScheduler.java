package multithreading;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MarketScheduler
{
    private final ScheduledExecutorService scheduler;

    private final StockPriceUpdater stockPriceUpdater;

    public MarketScheduler()
    {
        scheduler =
                Executors.newScheduledThreadPool(2);

        stockPriceUpdater =
                new StockPriceUpdater();
    }


    // ============================================
    // START MARKET
    // ============================================

    public void startMarket()
    {
        /*
         * Start after 2 seconds.
         *
         * Then update stock prices every 5 seconds.
         */

        scheduler.scheduleAtFixedRate(
                stockPriceUpdater,
                2,
                5,
                TimeUnit.SECONDS
        );

        System.out.println(
                "Market scheduler started."
        );
    }


    // ============================================
    // STOP MARKET
    // ============================================

    public void stopMarket()
    {
        stockPriceUpdater.stop();

        scheduler.shutdown();

        System.out.println(
                "Market scheduler stopped."
        );
    }
}