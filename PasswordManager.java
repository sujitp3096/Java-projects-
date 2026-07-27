
import java.io.*;
import java.util.Scanner;

public class PasswordManager {

    static final String FILE_NAME = "passwords.txt";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== PASSWORD MANAGER =====");
            System.out.println("1. Save Password");
            System.out.println("2. View Passwords");
            System.out.println("3. Search Password");
            System.out.println("4. Delete All Passwords");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Website/App: ");
                    String website = sc.nextLine();

                    System.out.print("Username: ");
                    String username = sc.nextLine();

                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    savePassword(website, username, password);
                    break;

                case 2:
                    viewPasswords();
                    break;

                case 3:
                    System.out.print("Enter Website to Search: ");
                    String search = sc.nextLine();
                    searchPassword(search);
                    break;

                case 4:
                    File file = new File(FILE_NAME);
                    if (file.delete()) {
                        System.out.println("All Passwords Deleted.");
                    } else {
                        System.out.println("No Password File Found.");
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

    static void savePassword(String website, String username, String password) {

        try {

            FileWriter fw = new FileWriter(FILE_NAME, true);

            fw.write("Website : " + website + "\n");
            fw.write("Username: " + username + "\n");
            fw.write("Password: " + password + "\n");
            fw.write("---------------------------\n");

            fw.close();

            System.out.println("Password Saved Successfully!");

        } catch (IOException e) {

            System.out.println("Error Saving Password.");

        }
    }

    static void viewPasswords() {

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println("No Passwords Saved.");
                return;
            }

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }

            reader.close();

        } catch (Exception e) {

            System.out.println("Error Reading File.");

        }
    }

    static void searchPassword(String website) {

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println("No Password File Found.");
                return;
            }

            Scanner reader = new Scanner(file);

            boolean found = false;

            while (reader.hasNextLine()) {

                String line = reader.nextLine();

                if (line.toLowerCase().contains(website.toLowerCase())) {
                    System.out.println(line);
                    System.out.println(reader.nextLine());
                    System.out.println(reader.nextLine());
                    System.out.println(reader.nextLine());
                    found = true;
                }
            }

            if (!found)
                System.out.println("Password Not Found.");

            reader.close();

        } catch (Exception e) {

            System.out.println("Error Searching Password.");

        }
    }
}
