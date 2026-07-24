\\RailwayReservationSystem.java
import java.util.ArrayList;
import java.util.Scanner;

class Passenger {
    int ticketId;
    String name;
    int age;
    String trainName;

    Passenger(int ticketId, String name, int age, String trainName) {
        this.ticketId = ticketId;
        this.name = name;
        this.age = age;
        this.trainName = trainName;
    }

    void display() {
        System.out.println("\n===== TICKET =====");
        System.out.println("Ticket ID : " + ticketId);
        System.out.println("Name      : " + name);
        System.out.println("Age       : " + age);
        System.out.println("Train     : " + trainName);
        System.out.println("==================");
    }
}

public class RailwayReservationSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Passenger> passengers = new ArrayList<>();

        String[] trains = {
                "Rajdhani Express",
                "Vande Bharat",
                "Shatabdi Express"
        };

        int[] seats = {10, 15, 20};

        int ticketCounter = 1001;

        while (true) {

            System.out.println("\n===== RAILWAY RESERVATION SYSTEM =====");
            System.out.println("1. View Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. View My Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.println("\nAvailable Trains:");

                    for (int i = 0; i < trains.length; i++) {
                        System.out.println(
                                (i + 1) + ". " +
                                trains[i] +
                                " | Seats: " +
                                seats[i]);
                    }

                    break;

                case 2:

                    System.out.println("\nSelect Train:");

                    for (int i = 0; i < trains.length; i++) {
                        System.out.println((i + 1) + ". " + trains[i]);
                    }

                    int trainChoice = sc.nextInt();

                    if (trainChoice < 1 || trainChoice > trains.length) {
                        System.out.println("Invalid Train Choice!");
                        break;
                    }

                    if (seats[trainChoice - 1] == 0) {
                        System.out.println("No Seats Available!");
                        break;
                    }

                    sc.nextLine();

                    System.out.print("Enter Passenger Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();

                    Passenger p = new Passenger(
                            ticketCounter,
                            name,
                            age,
                            trains[trainChoice - 1]);

                    passengers.add(p);

                    seats[trainChoice - 1]--;
                    ticketCounter++;

                    System.out.println("Ticket Booked Successfully!");
                    p.display();

                    break;

                case 3:

                    System.out.print("Enter Ticket ID: ");
                    int searchId = sc.nextInt();

                    boolean found = false;

                    for (Passenger passenger : passengers) {
                        if (passenger.ticketId == searchId) {
                            passenger.display();
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Ticket Not Found!");

                    break;

                case 4:

                    System.out.print("Enter Ticket ID: ");
                    int cancelId = sc.nextInt();

                    Passenger removePassenger = null;

                    for (Passenger passenger : passengers) {

                        if (passenger.ticketId == cancelId) {

                            removePassenger = passenger;

                            for (int i = 0; i < trains.length; i++) {
                                if (trains[i].equals(passenger.trainName)) {
                                    seats[i]++;
                                }
                            }

                            break;
                        }
                    }

                    if (removePassenger != null) {
                        passengers.remove(removePassenger);
                        System.out.println("Ticket Cancelled Successfully!");
                    } else {
                        System.out.println("Ticket Not Found!");
                    }

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
