
public class DBDepositThread extends Thread {

    private AccountDAO accountDAO;
    private int accountNumber;
    private double amount;

    public DBDepositThread(AccountDAO accountDAO,
                           int accountNumber,
                           double amount) {

        this.accountDAO = accountDAO;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Override
    public void run() {

        System.out.println(
            getName() + " is attempting to deposit Rs. " + amount
        );

        boolean success = accountDAO.deposit(accountNumber, amount);

        if (success) {
            System.out.println(getName() + ": Deposit successful!");
        } else {
            System.out.println(getName() + ": Deposit failed!");
        }
    }
}
