import java.util.Scanner;

class ATM {

    private double balance = 10000;
    private int pin = 1234;

    public boolean login(int enteredPin) {
        return enteredPin == pin;
    }

    public void checkBalance() {
        System.out.println("Current Balance: ₹" + balance);
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("₹" + amount + " Deposited Successfully.");
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("₹" + amount + " Withdrawn Successfully.");
        } else {
            System.out.println("Insufficient Balance!");
        }
    }

    public void changePin(int newPin) {
        pin = newPin;
        System.out.println("PIN Changed Successfully.");
    }
}

public class ATMManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ATM atm = new ATM();

        System.out.println("===== ATM LOGIN =====");
        System.out.print("Enter PIN: ");
        int enteredPin = sc.nextInt();

        if (!atm.login(enteredPin)) {
            System.out.println("Invalid PIN!");
            return;
        }

        while (true) {

            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    atm.checkBalance();
                    break;

                case 2:
                    System.out.print("Enter Deposit Amount: ");
                    atm.deposit(sc.nextDouble());
                    break;

                case 3:
                    System.out.print("Enter Withdraw Amount: ");
                    atm.withdraw(sc.nextDouble());
                    break;

                case 4:
                    System.out.print("Enter New PIN: ");
                    atm.changePin(sc.nextInt());
                    break;

                case 5:
                    System.out.println("Thank You for Using ATM.");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
