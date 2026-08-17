package filehandling;

import model.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TransactionLogger
{
    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE =
            "logs/transactions.log";

    // ============================================
    // LOG TRANSACTION
    // ============================================

    public void logTransaction(Transaction transaction)
    {
        createLogDirectory();

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(LOG_FILE, true)))
        {
            writer.write(
                    "Transaction ID: "
                            + transaction.getTransactionId()
                            + " | User ID: "
                            + transaction.getUserId()
                            + " | Stock ID: "
                            + transaction.getStockId()
                            + " | Type: "
                            + transaction.getTransactionType()
                            + " | Quantity: "
                            + transaction.getQuantity()
                            + " | Price: "
                            + transaction.getPrice()
                            + " | Total: "
                            + transaction.getTotalAmount()
            );

            writer.newLine();

        }
        catch (IOException e)
        {
            System.out.println(
                    "Failed to write transaction log."
            );

            e.printStackTrace();
        }
    }


    // ============================================
    // CREATE LOG DIRECTORY
    // ============================================

    private void createLogDirectory()
    {
        File directory =
                new File(LOG_DIRECTORY);

        if (!directory.exists())
        {
            directory.mkdirs();
        }
    }
}