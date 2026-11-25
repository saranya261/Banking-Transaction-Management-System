
public class AccountDAOTest {

    public static void main(String[] args) {

        AccountDAO dao = new AccountDAO();

        // Balance Enquiry Test
        double balance = dao.getBalance(101);

        System.out.println("Account Number: 101");
        System.out.println("Current Balance: Rs. " + balance);

        System.out.println("--------------------------------");

        // Admin Authentication Test
        boolean adminLogin =
                dao.authenticateAdmin("admin", "Admin@123");

        if (adminLogin) {
            System.out.println("Admin Authentication Successful!");
        } else {
            System.out.println("Admin Authentication Failed!");
        }
    }
}
