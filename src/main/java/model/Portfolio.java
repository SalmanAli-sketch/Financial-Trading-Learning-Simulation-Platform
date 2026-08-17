package model;

public class Portfolio
{
    private int portfolioId;
    private int userId;

    public Portfolio()
    {
    }

    public Portfolio(int portfolioId, int userId)
    {
        this.portfolioId = portfolioId;
        this.userId = userId;
    }

    public Portfolio(int userId)
    {
        this.userId = userId;
    }

    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId)
    {
        this.portfolioId = portfolioId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "Portfolio{" +
                "portfolioId=" + portfolioId +
                ", userId=" + userId +
                '}';
    }
}