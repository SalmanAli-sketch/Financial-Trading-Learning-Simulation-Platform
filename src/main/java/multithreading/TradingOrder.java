package multithreading;

public class TradingOrder
{
    private final int userId;
    private final int stockId;
    private final int quantity;
    private final String orderType;

    public TradingOrder(
            int userId,
            int stockId,
            int quantity,
            String orderType)
    {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.orderType = orderType;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getStockId()
    {
        return stockId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getOrderType()
    {
        return orderType;
    }
}