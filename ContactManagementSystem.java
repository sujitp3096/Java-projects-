
import java.util.ArrayList;
import java.util.Scanner;

class Contact {
    String name;
    String phone;

    Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    void display() {
        System.out.println("----------------------------");
        System.out.println("Name  : " + name);
        System.out.println("Phone : " + phone);
    }
}

public class ContactManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Contact> contacts = new ArrayList<>();

        while (true) {

            System.out.println("\n===== CONTACT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Update Contact");
            System.out.println("5. Delete Contact");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Phone Number: ");
                    String phone = sc.nextLine();

                    contacts.add(new Contact(name, phone));

                    System.out.println("Contact Added Successfully!");
                    break;

                case 2:

                    if (contacts.isEmpty()) {
                        System.out.println("No Contacts Available.");
                    } else {
                        for (Contact c : contacts) {
                            c.display();
                        }
                    }

                    break;

                case 3:

                    System.out.print("Enter Name to Search: ");
                    String search = sc.nextLine();

                    boolean found = false;

                    for (Contact c : contacts) {

                        if (c.name.equalsIgnoreCase(search)) {

                            c.display();
                            found = true;
                            break;

                        }
                    }

                    if (!found)
                        System.out.println("Contact Not Found.");

                    break;

                case 4:

                    System.out.print("Enter Name to Update: ");
                    String update = sc.nextLine();

                    found = false;

                    for (Contact c : contacts) {

                        if (c.name.equalsIgnoreCase(update)) {

                            System.out.print("Enter New Phone Number: ");
                            c.phone = sc.nextLine();

                            System.out.println("Contact Updated Successfully.");
                            found = true;
                            break;

                        }
                    }

                    if (!found)
                        System.out.println("Contact Not Found.");

                    break;

                case 5:

                    System.out.print("Enter Name to Delete: ");
                    String delete = sc.nextLine();

                    boolean removed = contacts.removeIf(
                            c -> c.name.equalsIgnoreCase(delete));

                    if (removed)
                        System.out.println("Contact Deleted Successfully.");
                    else
                        System.out.println("Contact Not Found.");

                    break;

                case 6:

                    System.out.println("Thank You!");
                    sc.close();
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice.");
            }
        }
    }
}
