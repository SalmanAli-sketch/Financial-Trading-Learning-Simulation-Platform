package model;

public class Holding
{
    private int holdingId;
    private int portfolioId;
    private int stockId;
    private int quantity;
    private double averagePrice;

    public Holding()
    {
    }

    public Holding(int holdingId, int portfolioId, int stockId,
                   int quantity, double averagePrice)
    {
        this.holdingId = holdingId;
        this.portfolioId = portfolioId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public Holding(int portfolioId, int stockId,
                   int quantity, double averagePrice)
    {
        this.portfolioId = portfolioId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public int getHoldingId()
    {
        return holdingId;
    }

    public void setHoldingId(int holdingId)
    {
        this.holdingId = holdingId;
    }

    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId)
    {
        this.portfolioId = portfolioId;
    }

    public int getStockId()
    {
        return stockId;
    }

    public void setStockId(int stockId)
    {
        this.stockId = stockId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public double getAveragePrice()
    {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice)
    {
        this.averagePrice = averagePrice;
    }

    @Override
    public String toString()
    {
        return "Holding{" +
                "holdingId=" + holdingId +
                ", portfolioId=" + portfolioId +
                ", stockId=" + stockId +
                ", quantity=" + quantity +
                ", averagePrice=" + averagePrice +
                '}';
    }
}