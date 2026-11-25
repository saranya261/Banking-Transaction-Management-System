
public class AccountTest {

    public static void main(String[] args) {

        Account account = new Account(101, "Sneha", 5000);

        System.out.println("Initial Balance: " + account.getBalance());

        account.deposit(2000);

        System.out.println("Balance after deposit: " + account.getBalance());

        account.withdraw(1000);

        System.out.println("Balance after withdrawal: " + account.getBalance());
    }
}
