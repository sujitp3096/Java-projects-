import java.util.ArrayList;
import java.util.Scanner;

class Food {
    int id;
    String name;
    double price;

    Food(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

public class FoodOrderingSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Food> menu = new ArrayList<>();

        menu.add(new Food(1, "Pizza", 250));
        menu.add(new Food(2, "Burger", 120));
        menu.add(new Food(3, "Pasta", 180));
        menu.add(new Food(4, "Sandwich", 100));
        menu.add(new Food(5, "Cold Drink", 60));

        double total = 0;

        while (true) {

            System.out.println("\n========= FOOD MENU =========");

            for (Food food : menu) {
                System.out.println(food.id + ". " + food.name + " - ₹" + food.price);
            }

            System.out.println("6. Generate Bill & Exit");

            System.out.print("\nEnter Choice: ");
            int choice = sc.nextInt();

            if (choice == 6)
                break;

            boolean found = false;

            for (Food food : menu) {

                if (food.id == choice) {

                    found = true;

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();

                    double amount = qty * food.price;

                    total += amount;

                    System.out.println(food.name + " Added.");
                    System.out.println("Amount: ₹" + amount);

                    break;
                }
            }

            if (!found)
                System.out.println("Invalid Choice.");
        }

        System.out.println("\n========== BILL ==========");
        System.out.println("Total Amount: ₹" + total);
        System.out.println("Thank You! Visit Again.");

        sc.close();
    }
}
