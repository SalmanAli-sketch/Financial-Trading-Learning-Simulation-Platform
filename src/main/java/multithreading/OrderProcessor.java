package multithreading;

import service.TradingService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderProcessor implements Runnable
{
    private final BlockingQueue<TradingOrder> orderQueue;
    private final TradingService tradingService;

    private volatile boolean running;

    public OrderProcessor()
    {
        orderQueue = new LinkedBlockingQueue<>();
        tradingService = new TradingService();
        running = true;
    }

    // ============================================
    // SUBMIT ORDER
    // ============================================

    public void submitOrder(TradingOrder order)
    {
        if (order == null)
        {
            return;
        }

        try
        {
            orderQueue.put(order);

            System.out.println(
                    "[ORDER QUEUE] Order submitted."
            );
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();

            System.out.println(
                    "Order submission interrupted."
            );
        }
    }

    // ============================================
    // PROCESS ORDERS
    // ============================================

    @Override
    public void run()
    {
        System.out.println(
                "Order Processor started."
        );

        while (running || !orderQueue.isEmpty())
        {
            try
            {
                TradingOrder order =
                        orderQueue.take();

                processOrder(order);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(
                "Order Processor stopped."
        );
    }

    // ============================================
    // PROCESS SINGLE ORDER
    // ============================================

    private void processOrder(TradingOrder order)
    {
        System.out.println(
                "[ORDER PROCESSOR] Processing "
                        + order.getOrderType()
                        + " order..."
        );

        boolean success;

        if (order.getOrderType().equalsIgnoreCase("BUY"))
        {
            success =
                    tradingService.buyStock(
                            order.getUserId(),
                            order.getStockId(),
                            order.getQuantity()
                    );
        }
        else
        {
            success =
                    tradingService.sellStock(
                            order.getUserId(),
                            order.getStockId(),
                            order.getQuantity()
                    );
        }

        if (success)
        {
            System.out.println(
                    "[ORDER PROCESSOR] Order completed."
            );
        }
        else
        {
            System.out.println(
                    "[ORDER PROCESSOR] Order failed."
            );
        }
    }

    // ============================================
    // STOP
    // ============================================

    public void stop()
    {
        running = false;
    }
}