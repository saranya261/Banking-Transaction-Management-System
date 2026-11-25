
public class WithdrawalTest {

    public static void main(String[] args) {

        // Create one shared BankService object
        BankService bank = new BankService();

        System.out.println("Initial Balance: Rs. " + bank.checkBalance());

        // Create two withdrawal threads
        WithdrawalThread t1 = new WithdrawalThread(bank, 4000);
        WithdrawalThread t2 = new WithdrawalThread(bank, 4000);

        // Give names to threads
        t1.setName("Customer 1");
        t2.setName("Customer 2");

        // Start both threads
        t1.start();
        t2.start();

        try {
            // Wait until both threads finish
            t1.join();
            t2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--------------------------------");
        System.out.println("Final Balance: Rs. " + bank.checkBalance());
    }
}
