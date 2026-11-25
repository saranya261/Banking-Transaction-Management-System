
public class TransferTest {

    public static void main(String[] args) {

        AccountDAO dao = new AccountDAO();

        int senderAccount = 101;
        int receiverAccount = 102;
        double amount = 1000;

        System.out.println("Before Transfer:");

        System.out.println("Sneha Balance: Rs. "
                + dao.getBalance(senderAccount));

        System.out.println("Rahul Balance: Rs. "
                + dao.getBalance(receiverAccount));

        boolean result = dao.transfer(
                senderAccount, receiverAccount, amount);

        if (result) {
            System.out.println("\nTransfer Successful!");

            System.out.println("After Transfer:");

            System.out.println("Sneha Balance: Rs. "
                    + dao.getBalance(senderAccount));

            System.out.println("Rahul Balance: Rs. "
                    + dao.getBalance(receiverAccount));

        } else {
            System.out.println("\nTransfer Failed!");
        }
    }
}
