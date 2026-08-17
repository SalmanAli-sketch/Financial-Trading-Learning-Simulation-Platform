package model;

public class Stock
{
    private int stockId;
    private String symbol;
    private String companyName;
    private double currentPrice;
    private int availableQuantity;

    public Stock()
    {
    }

    public Stock(int stockId, String symbol, String companyName,
                 double currentPrice, int availableQuantity)
    {
        this.stockId = stockId;
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.availableQuantity = availableQuantity;
    }

    public Stock(String symbol, String companyName,
                 double currentPrice, int availableQuantity)
    {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.availableQuantity = availableQuantity;
    }

    public int getStockId()
    {
        return stockId;
    }

    public void setStockId(int stockId)
    {
        this.stockId = stockId;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public double getCurrentPrice()
    {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice)
    {
        this.currentPrice = currentPrice;
    }

    public int getAvailableQuantity()
    {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity)
    {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString()
    {
        return "Stock{" +
                "stockId=" + stockId +
                ", symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", currentPrice=" + currentPrice +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
}