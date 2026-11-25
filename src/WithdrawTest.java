
public class WithdrawTest {

    public static void main(String[] args) {

        AccountDAO dao = new AccountDAO();

        int accountNumber = 101;
        double amount = 1000;

        boolean result = dao.withdraw(accountNumber, amount);

        if (result) {
            System.out.println("Withdrawal successful!");

            double balance = dao.getBalance(accountNumber);

            System.out.println("Remaining Balance: Rs. " + balance);

        } else {
            System.out.println("Withdrawal failed!");
            System.out.println("Insufficient balance or invalid account.");
        }
    }
}
