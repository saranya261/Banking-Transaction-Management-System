
public class DepositTest {

    public static void main(String[] args) {

        AccountDAO dao = new AccountDAO();

        int accountNumber = 101;
        double amount = 1000;

        boolean result = dao.deposit(accountNumber, amount);

        if (result) {
            System.out.println("Deposit successful!");

            double balance = dao.getBalance(accountNumber);

            System.out.println("Updated Balance: Rs. " + balance);
        } else {
            System.out.println("Deposit failed!");
        }
    }
}
