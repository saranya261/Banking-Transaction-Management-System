
public class DBTransferTest {

    public static void main(String[] args) {

        AccountDAO accountDAO = new AccountDAO();

        // Replace these with your test account numbers
        int senderAccount = 102;
        int receiverAccount = 101;

        System.out.println("Sender Initial Balance: Rs. "
                + accountDAO.getBalance(senderAccount));

        System.out.println("Receiver Initial Balance: Rs. "
                + accountDAO.getBalance(receiverAccount));

        // Create two transfer threads
        DBTransferThread t1 =
                new DBTransferThread(
                        accountDAO, senderAccount, receiverAccount, 700);

        DBTransferThread t2 =
                new DBTransferThread(
                        accountDAO, senderAccount, receiverAccount, 700);

        t1.setName("Transfer 1");
        t2.setName("Transfer 2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------");

        System.out.println("Sender Final Balance: Rs. "
                + accountDAO.getBalance(senderAccount));

        System.out.println("Receiver Final Balance: Rs. "
                + accountDAO.getBalance(receiverAccount));
    }
}
