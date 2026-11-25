
public class DBTransferThread extends Thread {

    private AccountDAO accountDAO;
    private int senderAccount;
    private int receiverAccount;
    private double amount;

    public DBTransferThread(
            AccountDAO accountDAO,
            int senderAccount,
            int receiverAccount,
            double amount) {

        this.accountDAO = accountDAO;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
    }

    @Override
    public void run() {

        System.out.println(
            getName() + " is attempting to transfer Rs. " + amount
        );

        boolean success = accountDAO.transfer(
            senderAccount, receiverAccount, amount
        );

        if (success) {
            System.out.println(getName() + ": Transfer successful!");
        } else {
            System.out.println(getName() + ": Transfer failed!");
        }
    }
}
