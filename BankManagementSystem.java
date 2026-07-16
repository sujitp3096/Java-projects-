import java.util.Scanner;

class BankAccount {
    String accountHolder;
    int accountNumber;
    double balance;

    BankAccount(String name, int accNo, double balance) {
        this.accountHolder = name;
        this.accountNumber = accNo;
        this.balance = balance;
    }

    void deposit(double amount) {
        balance += amount;
        System.out.println("₹" + amount + " Deposited Successfully.");
    }

    void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("₹" + amount + " Withdrawn Successfully.");
        } else {
            System.out.println("Insufficient Balance!");
        }
    }

    void checkBalance() {
        System.out.println("Current Balance: ₹" + balance);
    }

    void displayDetails() {
        System.out.println("\n----- Account Details -----");
        System.out.println("Account Holder : " + accountHolder);
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Balance        : ₹" + balance);
    }
}

public class BankManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        BankAccount account = new BankAccount("Sujit", 12345678, 10000);

        while (true) {
            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Account Details");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Amount: ");
                    account.deposit(sc.nextDouble());
                    break;

                case 2:
                    System.out.print("Enter Amount: ");
                    account.withdraw(sc.nextDouble());
                    break;

                case 3:
                    account.checkBalance();
                    break;

                case 4:
                    account.displayDetails();
                    break;

                case 5:
                    System.out.println("Thank You!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
