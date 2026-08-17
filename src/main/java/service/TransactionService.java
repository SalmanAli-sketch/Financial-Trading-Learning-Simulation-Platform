package service;

import dao.TransactionDAO;
import model.Transaction;

import java.util.List;

public class TransactionService
{
    private final TransactionDAO transactionDAO;


    // ============================================
    // CONSTRUCTOR
    // ============================================

    public TransactionService()
    {
        transactionDAO =
                new TransactionDAO();
    }


    // ============================================
    // GET USER TRANSACTIONS
    // ============================================

    public List<Transaction> getUserTransactions(
            int userId)
    {
        return transactionDAO.getUserTransactions(
                userId
        );
    }


    // ============================================
    // DISPLAY TRANSACTION HISTORY
    // ============================================

    public void displayTransactionHistory(
            int userId)
    {
        List<Transaction> transactions =
                getUserTransactions(userId);

        System.out.println();

        System.out.println(
                "========== TRANSACTION HISTORY =========="
        );

        if (transactions.isEmpty())
        {
            System.out.println(
                    "No transactions found."
            );

            return;
        }

        for (Transaction transaction :
                transactions)
        {
            System.out.println(
                    "Transaction ID : "
                            + transaction.getTransactionId()
            );

            System.out.println(
                    "Stock ID       : "
                            + transaction.getStockId()
            );

            System.out.println(
                    "Type           : "
                            + transaction.getTransactionType()
            );

            System.out.println(
                    "Quantity       : "
                            + transaction.getQuantity()
            );

            System.out.println(
                    "Price          : ₹"
                            + transaction.getPrice()
            );

            System.out.println(
                    "Total Amount   : ₹"
                            + transaction.getTotalAmount()
            );

            System.out.println(
                    "Time           : "
                            + transaction.getTransactionTime()
            );

            System.out.println(
                    "-----------------------------------------"
            );
        }
    }
}