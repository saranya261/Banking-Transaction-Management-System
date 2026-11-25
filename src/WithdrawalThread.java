
public class WithdrawalThread extends Thread {

    private BankService bankService;
    private double amount;

    public WithdrawalThread(BankService bankService, double amount) {

        this.bankService = bankService;
        this.amount = amount;
    }

    @Override
    public void run() {

        System.out.println(
            Thread.currentThread().getName() + " is trying to withdraw Rs. " + amount
        );

        bankService.withdraw(amount);

        System.out.println(
            Thread.currentThread().getName() + " completed withdrawal attempt."
        );

        System.out.println(
            "Remaining Balance: Rs. " + bankService.checkBalance()
        );
    }
}
