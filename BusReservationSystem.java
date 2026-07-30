import java.util.Scanner;

public class BusReservationSystem {
    static final int TOTAL_SEATS = 10;
    static boolean[] booked = new boolean[TOTAL_SEATS];
    static String[] passenger = new String[TOTAL_SEATS];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== BUS RESERVATION SYSTEM =====");
            System.out.println("1. View Seats");
            System.out.println("2. Book Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Search Passenger");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewSeats();
                    break;
                case 2:
                    System.out.print("Enter Seat Number (1-10): ");
                    int seat = sc.nextInt();
                    sc.nextLine();
                    if (seat < 1 || seat > TOTAL_SEATS)
                        System.out.println("Invalid Seat Number.");
                    else if (booked[seat-1])
                        System.out.println("Seat Already Booked.");
                    else {
                        System.out.print("Enter Passenger Name: ");
                        passenger[seat-1] = sc.nextLine();
                        booked[seat-1] = true;
                        System.out.println("Seat Booked Successfully!");
                    }
                    break;
                case 3:
                    System.out.print("Enter Seat Number: ");
                    seat = sc.nextInt();
                    if (seat < 1 || seat > TOTAL_SEATS)
                        System.out.println("Invalid Seat.");
                    else if (!booked[seat-1])
                        System.out.println("Seat Already Empty.");
                    else {
                        booked[seat-1] = false;
                        passenger[seat-1] = null;
                        System.out.println("Booking Cancelled.");
                    }
                    break;
                case 4:
                    sc.nextLine();
                    System.out.print("Enter Passenger Name: ");
                    String name = sc.nextLine();
                    boolean found = false;
                    for(int i=0;i<TOTAL_SEATS;i++){
                        if(booked[i] && passenger[i].equalsIgnoreCase(name)){
                            System.out.println(name + " booked Seat " + (i+1));
                            found = true;
                        }
                    }
                    if(!found) System.out.println("Passenger Not Found.");
                    break;
                case 5:
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }

    static void viewSeats(){
        for(int i=0;i<TOTAL_SEATS;i++){
            if(booked[i])
                System.out.println("Seat "+(i+1)+": Booked ("+passenger[i]+")");
            else
                System.out.println("Seat "+(i+1)+": Available");
        }
    }
}

Library
/
BusReservationSystem.java
import java.util.Scanner;

public class BusReservationSystem {
    static final int TOTAL_SEATS = 10;
    static boolean[] booked = new boolean[TOTAL_SEATS];
    static String[] passenger = new String[TOTAL_SEATS];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== BUS RESERVATION SYSTEM =====");
            System.out.println("1. View Seats");
            System.out.println("2. Book Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Search Passenger");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewSeats();
                    break;
                case 2:
                    System.out.print("Enter Seat Number (1-10): ");
                    int seat = sc.nextInt();
                    sc.nextLine();
                    if (seat < 1 || seat > TOTAL_SEATS)
                        System.out.println("Invalid Seat Number.");
                    else if (booked[seat-1])
                        System.out.println("Seat Already Booked.");
                    else {
                        System.out.print("Enter Passenger Name: ");
                        passenger[seat-1] = sc.nextLine();
                        booked[seat-1] = true;
                        System.out.println("Seat Booked Successfully!");
                    }
                    break;
                case 3:
                    System.out.print("Enter Seat Number: ");
                    seat = sc.nextInt();
                    if (seat < 1 || seat > TOTAL_SEATS)
                        System.out.println("Invalid Seat.");
                    else if (!booked[seat-1])
                        System.out.println("Seat Already Empty.");
                    else {
                        booked[seat-1] = false;
                        passenger[seat-1] = null;
                        System.out.println("Booking Cancelled.");
                    }
                    break;
                case 4:
                    sc.nextLine();
                    System.out.print("Enter Passenger Name: ");
                    String name = sc.nextLine();
                    boolean found = false;
                    for(int i=0;i<TOTAL_SEATS;i++){
                        if(booked[i] && passenger[i].equalsIgnoreCase(name)){
                            System.out.println(name + " booked Seat " + (i+1));
                            found = true;
                        }
                    }
                    if(!found) System.out.println("Passenger Not Found.");
                    break;
                case 5:
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }

    static void viewSeats(){
        for(int i=0;i<TOTAL_SEATS;i++){
            if(booked[i])
                System.out.println("Seat "+(i+1)+": Booked ("+passenger[i]+")");
            else
                System.out.println("Seat "+(i+1)+": Available");
        }
    }
}
