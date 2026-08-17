package model;

public class User
{
    private int userId;
    private String username;
    private String email;
    private String password;
    private double balance;

    public User()
    {
    }

    public User(int userId, String username, String email,
                String password, double balance)
    {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public User(String username, String email,
                String password, double balance)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }
}