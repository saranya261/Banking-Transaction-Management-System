
import java.util.ArrayList;

public class TransactionHistoryTest {

    public static void main(String[] args) {

        AccountDAO dao = new AccountDAO();

        int accountNumber = 101;

        ArrayList<String> history =
                dao.getTransactionHistory(accountNumber);

        System.out.println("===== TRANSACTION HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String transaction : history) {
                System.out.println(transaction);
            }
        }
    }
}
