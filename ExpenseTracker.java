import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Expense Tracker - Console based Java application
 * Features:
 *  - Add expenses with category, amount, date, and note
 *  - View all expenses
 *  - View category-wise totals
 *  - Delete an expense
 *  - Save/Load expensesto/from a file (expenses.txt) so data persists
 */
public class ExpenseTracker {

    static final String FILE_NAME = "expenses.txt";
    static List<Expense> expenses = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadExpenses();
        boolean running = true;

        System.out.println("===== Welcome to Expense Tracker =====");

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    viewCategoryTotals();
                    break;
                case 4:
                    deleteExpense();
                    break;
                case 5:
                    saveExpenses();
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    static void printMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("1. Add Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. View Category-wise Totals");
        System.out.println("4. Delete Expense");
        System.out.println("5. Save & Exit");
        System.out.println("--------------------------------");
    }

    static void addExpense() {
        System.out.print("Enter category (e.g. Food, Travel, Bills): ");
        String category = sc.nextLine().trim();

        double amount = readDouble("Enter amount: ");

        System.out.print("Enter note (optional): ");
        String note = sc.nextLine().trim();

        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

        int id = expenses.size() + 1;
        Expense e = new Expense(id, category, amount, date, note);
        expenses.add(e);

        System.out.println("Expense added successfully!");
        saveExpenses();
    }

    static void viewAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println("\nID  | Category      | Amount    | Date              | Note");
        System.out.println("----------------------------------------------------------------");
        double total = 0;
        for (Expense e : expenses) {
            System.out.printf("%-4d| %-14s| %-10.2f| %-18s| %s%n",
                    e.id, e.category, e.amount, e.date, e.note);
            total += e.amount;
        }
        System.out.println("----------------------------------------------------------------");
        System.out.printf("Total Spent: %.2f%n", total);
    }

    static void viewCategoryTotals() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        Map<String, Double> totals = new LinkedHashMap<>();
        for (Expense e : expenses) {
            totals.merge(e.category, e.amount, Double::sum);
        }

        System.out.println("\nCategory-wise Totals:");
        System.out.println("----------------------------------");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.printf("%-15s: %.2f%n", entry.getKey(), entry.getValue());
        }
        System.out.println("----------------------------------");
    }

    static void deleteExpense() {
        viewAllExpenses();
        if (expenses.isEmpty()) return;

        int id = readInt("Enter ID of expense to delete: ");
        boolean removed = expenses.removeIf(e -> e.id == id);

        if (removed) {
            System.out.println("Expense deleted.");
            reassignIds();
            saveExpenses();
        } else {
            System.out.println("No expense found with that ID.");
        }
    }

    static void reassignIds() {
        int id = 1;
        for (Expense e : expenses) {
            e.id = id++;
        }
    }

    static void saveExpenses() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                pw.println(e.toFileString());
            }
        } catch (IOException ex) {
            System.out.println("Error saving expenses: " + ex.getMessage());
        }
    }

    static void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Expense e = Expense.fromFileString(line);
                if (e != null) expenses.add(e);
            }
        } catch (IOException ex) {
            System.out.println("Error loading expenses: " + ex.getMessage());
        }
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid amount: ");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }
}

class Expense {
    int id;
    String category;
    double amount;
    String date;
    String note;

    public Expense(int id, String category, double amount, String date, String note) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    // Format for saving to file: id|category|amount|date|note
    public String toFileString() {
        return id + "|" + category + "|" + amount + "|" + date + "|" + note;
    }

    public static Expense fromFileString(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            int id = Integer.parseInt(parts[0]);
            String category = parts[1];
            double amount = Double.parseDouble(parts[2]);
            String date = parts[3];
            String note = parts.length > 4 ? parts[4] : "";
            return new Expense(id, category, amount, date, note);
        } catch (Exception e) {
            return null;
        }
    }
}
