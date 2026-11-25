
public class DBDepositTest {

    public static void main(String[] args) {

        AccountDAO accountDAO = new AccountDAO();

        // Use account 102 for this test
        int accountNumber = 102;

        System.out.println("Initial Balance: Rs. "
                + accountDAO.getBalance(accountNumber));

        // Create two deposit threads
        DBDepositThread t1 =
                new DBDepositThread(accountDAO, accountNumber, 500);

        DBDepositThread t2 =
                new DBDepositThread(accountDAO, accountNumber, 500);

        t1.setName("Deposit 1");
        t2.setName("Deposit 2");

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
    }
}
