package service;

import dao.UserDAO;
import model.User;

public class UserService
{
    private final UserDAO userDAO;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public UserService()
    {
        userDAO = new UserDAO();
    }


    // ============================================
    // CREATE USER
    // ============================================

    public boolean createUser(String username,
                              String email,
                              String password,
                              double initialBalance)
    {
        if (username == null || username.isBlank())
        {
            System.out.println("Username cannot be empty.");
            return false;
        }

        if (email == null || email.isBlank())
        {
            System.out.println("Email cannot be empty.");
            return false;
        }

        if (password == null || password.isBlank())
        {
            System.out.println("Password cannot be empty.");
            return false;
        }

        if (initialBalance < 0)
        {
            System.out.println("Initial balance cannot be negative.");
            return false;
        }

        User existingUser =
                userDAO.getUserByUsername(username);

        if (existingUser != null)
        {
            System.out.println("Username already exists.");
            return false;
        }

        User user = new User(
                username,
                email,
                password,
                initialBalance
        );

        return userDAO.createUser(user);
    }


    // ============================================
    // GET USER BY ID
    // ============================================

    public User getUser(int userId)
    {
        return userDAO.getUserById(userId);
    }


    // ============================================
    // LOGIN
    // ============================================

    public User login(String username, String password)
    {
        User user =
                userDAO.getUserByUsername(username);

        if (user == null)
        {
            System.out.println("User not found.");
            return null;
        }

        if (!user.getPassword().equals(password))
        {
            System.out.println("Invalid password.");
            return null;
        }

        return user;
    }


    // ============================================
    // ADD BALANCE
    // ============================================

    public boolean addBalance(int userId, double amount)
    {
        if (amount <= 0)
        {
            System.out.println(
                    "Amount must be greater than zero."
            );

            return false;
        }

        User user =
                userDAO.getUserById(userId);

        if (user == null)
        {
            System.out.println("User not found.");
            return false;
        }

        double newBalance =
                user.getBalance() + amount;

        return userDAO.updateBalance(
                userId,
                newBalance
        );
    }
}