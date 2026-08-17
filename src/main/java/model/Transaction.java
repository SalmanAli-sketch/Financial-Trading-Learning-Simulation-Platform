package model;

import java.time.LocalDateTime;

public class Transaction
{
    private int transactionId;
    private int userId;
    private int stockId;
    private String transactionType;
    private int quantity;
    private double price;
    private double totalAmount;
    private LocalDateTime transactionTime;

    public Transaction()
    {
    }

    public Transaction(int transactionId, int userId, int stockId,
                       String transactionType, int quantity,
                       double price, double totalAmount,
                       LocalDateTime transactionTime)
    {
        this.transactionId = transactionId;
        this.userId = userId;
        this.stockId = stockId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.transactionTime = transactionTime;
    }

    public Transaction(int userId, int stockId,
                       String transactionType, int quantity,
                       double price, double totalAmount)
    {
        this.userId = userId;
        this.stockId = stockId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
    }

    public int getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(int transactionId)
    {
        this.transactionId = transactionId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getStockId()
    {
        return stockId;
    }

    public void setStockId(int stockId)
    {
        this.stockId = stockId;
    }

    public String getTransactionType()
    {
        return transactionType;
    }

    public void setTransactionType(String transactionType)
    {
        this.transactionType = transactionType;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTransactionTime()
    {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime)
    {
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString()
    {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", stockId=" + stockId +
                ", transactionType='" + transactionType + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalAmount=" + totalAmount +
                ", transactionTime=" + transactionTime +
                '}';
    }
}