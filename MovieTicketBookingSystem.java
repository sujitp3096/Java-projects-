import java.util.Scanner;

public class MovieTicketBookingSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] movies = {
                "Avengers",
                "Pushpa 2",
                "Kalki 2898 AD"
        };

        int[] price = {250, 200, 300};

        int[] availableSeats = {20, 25, 15};

        while (true) {

            System.out.println("\n======= MOVIE TICKET BOOKING =======");

            for (int i = 0; i < movies.length; i++) {
                System.out.println((i + 1) + ". " + movies[i] +
                        " | Ticket Price ₹" + price[i] +
                        " | Seats Available: " + availableSeats[i]);
            }

            System.out.println("4. Exit");

            System.out.print("\nSelect Movie: ");
            int choice = sc.nextInt();

            if (choice == 4) {
                System.out.println("Thank You!");
                break;
            }

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid Choice!");
                continue;
            }

            int index = choice - 1;

            System.out.print("Enter Number of Tickets: ");
            int tickets = sc.nextInt();

            if (tickets <= availableSeats[index]) {

                availableSeats[index] -= tickets;

                int total = tickets * price[index];

                System.out.println("\n========= TICKET =========");
                System.out.println("Movie : " + movies[index]);
                System.out.println("Tickets : " + tickets);
                System.out.println("Price per Ticket : ₹" + price[index]);
                System.out.println("Total Amount : ₹" + total);
                System.out.println("Booking Successful!");
                System.out.println("==========================");

            } else {

                System.out.println("Sorry! Seats Not Available.");
            }

        }

        sc.close();
    }
}
