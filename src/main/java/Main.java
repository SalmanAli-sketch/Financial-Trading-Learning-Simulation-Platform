import dao.StockDAO;
import filehandling.CSVHandler;
import filehandling.ReportGenerator;
import model.Stock;
import model.User;
import multithreading.MarketScheduler;
import multithreading.OrderProcessor;
import multithreading.TradingOrder;
import service.PortfolioService;
import service.TransactionService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main
{
    private static final Scanner scanner =
            new Scanner(System.in);

    private static final UserService userService =
            new UserService();

    private static final PortfolioService portfolioService =
            new PortfolioService();

    private static final TransactionService transactionService =
            new TransactionService();

    private static final CSVHandler csvHandler =
            new CSVHandler();

    private static final ReportGenerator reportGenerator =
            new ReportGenerator();

    private static final MarketScheduler marketScheduler =
            new MarketScheduler();

    private static final OrderProcessor orderProcessor =
            new OrderProcessor();

    private static Thread orderProcessorThread;


    // ============================================
    // MAIN
    // ============================================

    public static void main(String[] args)
    {
        startBackgroundServices();

        System.out.println();
        System.out.println(
                "=========================================="
        );

        System.out.println(
                "        STOCK TRADING SIMULATOR"
        );

        System.out.println(
                "=========================================="
        );

        boolean running = true;

        while (running)
        {
            showMainMenu();

            int choice =
                    readInt("Enter your choice: ");

            switch (choice)
            {
                case 1:
                    createUser();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    displayStocks();
                    break;

                case 4:
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice."
                    );
            }
        }

        stopBackgroundServices();

        scanner.close();

        System.out.println(
                "Application closed."
        );
    }


    // ============================================
    // START BACKGROUND SERVICES
    // ============================================

    private static void startBackgroundServices()
    {
        System.out.println(
                "Starting background services..."
        );

        marketScheduler.startMarket();

        orderProcessorThread =
                new Thread(
                        orderProcessor,
                        "Order-Processor-Thread"
                );

        orderProcessorThread.start();

        System.out.println(
                "Background services started."
        );
    }


    // ============================================
    // STOP BACKGROUND SERVICES
    // ============================================

    private static void stopBackgroundServices()
    {
        System.out.println(
                "Stopping background services..."
        );

        marketScheduler.stopMarket();

        orderProcessor.stop();

        if (orderProcessorThread != null)
        {
            orderProcessorThread.interrupt();
        }

        System.out.println(
                "Background services stopped."
        );
    }


    // ============================================
    // MAIN MENU
    // ============================================

    private static void showMainMenu()
    {
        System.out.println();
        System.out.println(
                "============== MAIN MENU =============="
        );

        System.out.println("1. Create User");
        System.out.println("2. Login");
        System.out.println("3. View Stocks");
        System.out.println("4. Exit");

        System.out.println(
                "========================================"
        );
    }


    // ============================================
    // CREATE USER
    // ============================================

    private static void createUser()
    {
        System.out.println();
        System.out.println(
                "========== CREATE USER =========="
        );

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        double balance =
                readDouble("Initial Balance: ");

        boolean created =
                userService.createUser(
                        username,
                        email,
                        password,
                        balance
                );

        if (created)
        {
            System.out.println(
                    "User created successfully."
            );
        }
        else
        {
            System.out.println(
                    "Failed to create user."
            );
        }
    }


    // ============================================
    // LOGIN
    // ============================================

    private static void login()
    {
        System.out.println();
        System.out.println(
                "============== LOGIN =============="
        );

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user =
                userService.login(
                        username,
                        password
                );

        if (user == null)
        {
            System.out.println(
                    "Login failed."
            );

            return;
        }

        System.out.println(
                "Login successful."
        );

        System.out.println(
                "Welcome, "
                        + user.getUsername()
        );

        userMenu(user);
    }


    // ============================================
    // USER MENU
    // ============================================

    private static void userMenu(User user)
    {
        boolean loggedIn = true;

        while (loggedIn)
        {
            System.out.println();
            System.out.println(
                    "=========== USER MENU ==========="
            );

            System.out.println("1. View Balance");
            System.out.println("2. Add Balance");
            System.out.println("3. View Stocks");
            System.out.println("4. Buy Stock");
            System.out.println("5. Sell Stock");
            System.out.println("6. View Portfolio");
            System.out.println("7. View Transactions");
            System.out.println("8. Export Portfolio CSV");
            System.out.println("9. Generate Trading Report");
            System.out.println("10. Logout");

            System.out.println(
                    "================================="
            );

            int choice =
                    readInt("Enter your choice: ");

            switch (choice)
            {
                case 1:
                    viewBalance(user);
                    break;

                case 2:
                    addBalance(user);
                    break;

                case 3:
                    displayStocks();
                    break;

                case 4:
                    buyStock(user);
                    break;

                case 5:
                    sellStock(user);
                    break;

                case 6:
                    viewPortfolio(user);
                    break;

                case 7:
                    viewTransactions(user);
                    break;

                case 8:
                    exportPortfolio(user);
                    break;

                case 9:
                    generateReport(user);
                    break;

                case 10:
                    loggedIn = false;

                    System.out.println(
                            "Logged out successfully."
                    );

                    break;

                default:
                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }


    // ============================================
    // VIEW BALANCE
    // ============================================

    private static void viewBalance(User user)
    {
        User updatedUser =
                userService.getUser(
                        user.getUserId()
                );

        if (updatedUser == null)
        {
            System.out.println(
                    "Unable to retrieve user."
            );

            return;
        }

        user.setBalance(
                updatedUser.getBalance()
        );

        System.out.println(
                "Current Balance: ₹"
                        + String.format(
                        "%.2f",
                        user.getBalance()
                )
        );
    }


    // ============================================
    // ADD BALANCE
    // ============================================

    private static void addBalance(User user)
    {
        double amount =
                readDouble(
                        "Enter amount to add: "
                );

        if (amount <= 0)
        {
            System.out.println(
                    "Amount must be greater than zero."
            );

            return;
        }

        boolean success =
                userService.addBalance(
                        user.getUserId(),
                        amount
                );

        if (success)
        {
            user.setBalance(
                    user.getBalance() + amount
            );

            System.out.println(
                    "Balance added successfully."
            );
        }
    }


    // ============================================
    // DISPLAY STOCKS
    // ============================================

    private static void displayStocks()
    {
        StockDAO stockDAO =
                new StockDAO();

        List<Stock> stocks =
                stockDAO.getAllStocks();

        System.out.println();
        System.out.println(
                "=========== AVAILABLE STOCKS ==========="
        );

        if (stocks.isEmpty())
        {
            System.out.println(
                    "No stocks found."
            );

            return;
        }

        for (Stock stock : stocks)
        {
            System.out.println(
                    "ID: "
                            + stock.getStockId()
                            + " | Symbol: "
                            + stock.getSymbol()
                            + " | Company: "
                            + stock.getCompanyName()
                            + " | Price: ₹"
                            + stock.getCurrentPrice()
                            + " | Available: "
                            + stock.getAvailableQuantity()
            );
        }

        System.out.println(
                "========================================="
        );
    }


    // ============================================
    // BUY STOCK
    // ============================================

    private static void buyStock(User user)
    {
        int stockId =
                readInt("Enter Stock ID: ");

        int quantity =
                readInt("Enter Quantity: ");

        if (quantity <= 0)
        {
            System.out.println(
                    "Quantity must be greater than zero."
            );

            return;
        }

        TradingOrder order =
                new TradingOrder(
                        user.getUserId(),
                        stockId,
                        quantity,
                        "BUY"
                );

        orderProcessor.submitOrder(order);

        System.out.println(
                "BUY order submitted for processing."
        );
    }


    // ============================================
    // SELL STOCK
    // ============================================

    private static void sellStock(User user)
    {
        int stockId =
                readInt("Enter Stock ID: ");

        int quantity =
                readInt("Enter Quantity: ");

        if (quantity <= 0)
        {
            System.out.println(
                    "Quantity must be greater than zero."
            );

            return;
        }

        TradingOrder order =
                new TradingOrder(
                        user.getUserId(),
                        stockId,
                        quantity,
                        "SELL"
                );

        orderProcessor.submitOrder(order);

        System.out.println(
                "SELL order submitted for processing."
        );
    }


    // ============================================
    // VIEW PORTFOLIO
    // ============================================

    private static void viewPortfolio(User user)
    {
        portfolioService.displayHoldings(
                user.getUserId()
        );

        double invested =
                portfolioService.calculateInvestedAmount(
                        user.getUserId()
                );

        double currentValue =
                portfolioService.calculatePortfolioValue(
                        user.getUserId()
                );

        double profitLoss =
                portfolioService.calculateProfitLoss(
                        user.getUserId()
                );

        System.out.println();
        System.out.println(
                "Invested Amount : ₹"
                        + String.format(
                        "%.2f",
                        invested
                )
        );

        System.out.println(
                "Current Value   : ₹"
                        + String.format(
                        "%.2f",
                        currentValue
                )
        );

        System.out.println(
                "Profit / Loss   : ₹"
                        + String.format(
                        "%.2f",
                        profitLoss
                )
        );
    }


    // ============================================
    // VIEW TRANSACTIONS
    // ============================================

    private static void viewTransactions(User user)
    {
        transactionService.displayTransactionHistory(
                user.getUserId()
        );
    }


    // ============================================
    // EXPORT PORTFOLIO
    // ============================================

    private static void exportPortfolio(User user)
    {
        csvHandler.exportPortfolio(
                user.getUserId()
        );
    }


    // ============================================
    // GENERATE REPORT
    // ============================================

    private static void generateReport(User user)
    {
        reportGenerator.generateReport(
                user.getUserId()
        );
    }


    // ============================================
    // READ INTEGER
    // ============================================

    private static int readInt(String message)
    {
        while (true)
        {
            try
            {
                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine()
                );
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }


    // ============================================
    // READ DOUBLE
    // ============================================

    private static double readDouble(String message)
    {
        while (true)
        {
            try
            {
                System.out.print(message);

                return Double.parseDouble(
                        scanner.nextLine()
                );
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                        "Please enter a valid amount."
                );
            }
        }
    }
}