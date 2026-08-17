package filehandling;

import dao.TransactionDAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ReportGenerator
{
    private static final String REPORT_DIRECTORY =
            "reports";

    private static final String REPORT_FILE =
            "reports/daily_report.txt";

    private final TransactionDAO transactionDAO;

    public ReportGenerator()
    {
        transactionDAO = new TransactionDAO();
    }


    // ============================================
    // GENERATE DAILY REPORT
    // ============================================

    public void generateReport(int userId)
    {
        createReportDirectory();

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(REPORT_FILE)))
        {
            writer.write("====================================");
            writer.newLine();

            writer.write("     STOCK TRADING DAILY REPORT");
            writer.newLine();

            writer.write("====================================");
            writer.newLine();

            writer.write(
                    "Generated At: "
                            + LocalDateTime.now()
            );

            writer.newLine();
            writer.newLine();

            writer.write(
                    "User ID: "
                            + userId
            );

            writer.newLine();

            writer.write("------------------------------------");
            writer.newLine();

            writer.write(
                    "Transaction History:"
            );

            writer.newLine();

            writer.write("------------------------------------");
            writer.newLine();

            /*
             * Current TransactionDAO prints directly.
             * Later we will change it to return
             * List<Transaction> so the report can
             * write proper transaction data here.
             */

            transactionDAO.getUserTransactions(userId);

            writer.write("------------------------------------");
            writer.newLine();

            writer.write("Report Generated Successfully.");
            writer.newLine();

            writer.write("====================================");
            writer.newLine();
        }
        catch (IOException e)
        {
            System.out.println(
                    "Failed to generate report."
            );

            e.printStackTrace();
        }
    }


    // ============================================
    // CREATE REPORT DIRECTORY
    // ============================================

    private void createReportDirectory()
    {
        File directory =
                new File(REPORT_DIRECTORY);

        if (!directory.exists())
        {
            directory.mkdirs();
        }
    }
}