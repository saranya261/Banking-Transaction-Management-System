
import java.util.ArrayList;

public class DBWithdrawalTest {

    public static void main(String[] args) {

        AccountDAO accountDAO = new AccountDAO();

        int accountNumber = 102;

        System.out.println("Initial Balance: Rs. "
                + accountDAO.getBalance(accountNumber));

        DBWithdrawalThread t1 =
                new DBWithdrawalThread(accountDAO, accountNumber, 4000);

        DBWithdrawalThread t2 =
                new DBWithdrawalThread(accountDAO, accountNumber, 4000);

        t1.setName("Customer 1");
        t2.setName("Customer 2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------");

        System.out.println("Final Balance: Rs. "
                + accountDAO.getBalance(accountNumber));

        // Display transaction history
        System.out.println("\nTransaction History:");

        ArrayList<String> history =
                accountDAO.getTransactionHistory(accountNumber);

        for (String transaction : history) {
            System.out.println(transaction);
        }
    }
}
