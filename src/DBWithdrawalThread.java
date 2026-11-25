
public class DBWithdrawalThread extends Thread {

    private AccountDAO accountDAO;
    private int accountNumber;
    private double amount;

    public DBWithdrawalThread(AccountDAO accountDAO,
                              int accountNumber,
                              double amount) {

        this.accountDAO = accountDAO;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Override
    public void run() {

        System.out.println(
            getName() + " is trying to withdraw Rs. " + amount
        );

        boolean success = accountDAO.withdraw(accountNumber, amount);

        if (success) {
            System.out.println(getName() + ": Withdrawal successful!");
        } else {
            System.out.println(getName() + ": Withdrawal failed!");
        }
    }
}
