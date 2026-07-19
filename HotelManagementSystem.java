import java.util.ArrayList;
import java.util.Scanner;

class Room {
    int roomNo;
    String customerName;
    boolean booked;

    Room(int roomNo) {
        this.roomNo = roomNo;
        this.customerName = "";
        this.booked = false;
    }

    void display() {
        System.out.println("Room No : " + roomNo);
        System.out.println("Status  : " + (booked ? "Booked" : "Available"));

        if (booked)
            System.out.println("Customer: " + customerName);

        System.out.println("----------------------------");
    }
}

public class HotelManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Room> rooms = new ArrayList<>();

        // Create 10 Rooms
        for (int i = 101; i <= 110; i++) {
            rooms.add(new Room(i));
        }

        while (true) {

            System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Check Out");
            System.out.println("4. Search Room");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    for (Room r : rooms)
                        r.display();

                    break;

                case 2:

                    System.out.print("Enter Room Number: ");
                    int roomNo = sc.nextInt();
                    sc.nextLine();

                    boolean found = false;

                    for (Room r : rooms) {

                        if (r.roomNo == roomNo) {

                            found = true;

                            if (!r.booked) {

                                System.out.print("Enter Customer Name: ");
                                r.customerName = sc.nextLine();

                                r.booked = true;

                                System.out.println("Room Booked Successfully.");

                            } else {

                                System.out.println("Room Already Booked.");
                            }

                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Room Not Found.");

                    break;

                case 3:

                    System.out.print("Enter Room Number: ");
                    roomNo = sc.nextInt();

                    found = false;

                    for (Room r : rooms) {

                        if (r.roomNo == roomNo) {

                            found = true;

                            if (r.booked) {

                                r.booked = false;
                                r.customerName = "";

                                System.out.println("Check Out Successful.");

                            } else {

                                System.out.println("Room is Already Empty.");
                            }

                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Room Not Found.");

                    break;

                case 4:

                    System.out.print("Enter Room Number: ");
                    roomNo = sc.nextInt();

                    found = false;

                    for (Room r : rooms) {

                        if (r.roomNo == roomNo) {

                            r.display();
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Room Not Found.");

                    break;

                case 5:

                    System.out.println("Thank You!");
                    sc.close();
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice.");

            }
        }
    }
              }
