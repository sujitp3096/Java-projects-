import java.util.ArrayList;
import java.util.Scanner;

class Book {
    int id;
    String title;
    String author;
    boolean issued;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = false;
    }

    void display() {
        System.out.println("\nBook ID : " + id);
        System.out.println("Title   : " + title);
        System.out.println("Author  : " + author);
        System.out.println("Status  : " + (issued ? "Issued" : "Available"));
    }
}

public class LibraryManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Book> books = new ArrayList<>();

        while (true) {

            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Delete Book");
            System.out.println("7. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();

                    System.out.print("Enter Author Name: ");
                    String author = sc.nextLine();

                    books.add(new Book(id, title, author));

                    System.out.println("Book Added Successfully!");
                    break;

                case 2:

                    if (books.isEmpty()) {
                        System.out.println("No Books Available.");
                    } else {
                        for (Book b : books)
                            b.display();
                    }

                    break;

                case 3:

                    System.out.print("Enter Book ID: ");
                    int searchId = sc.nextInt();

                    boolean found = false;

                    for (Book b : books) {

                        if (b.id == searchId) {
                            b.display();
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Book Not Found!");

                    break;

                case 4:

                    System.out.print("Enter Book ID to Issue: ");
                    int issueId = sc.nextInt();

                    for (Book b : books) {

                        if (b.id == issueId) {

                            if (!b.issued) {
                                b.issued = true;
                                System.out.println("Book Issued Successfully!");
                            } else {
                                System.out.println("Book Already Issued.");
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Book Not Found!");

                    break;

                case 5:

                    System.out.print("Enter Book ID to Return: ");
                    int returnId = sc.nextInt();

                    found = false;

                    for (Book b : books) {

                        if (b.id == returnId) {

                            if (b.issued) {
                                b.issued = false;
                                System.out.println("Book Returned Successfully!");
                            } else {
                                System.out.println("Book is Already Available.");
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        System.out.println("Book Not Found!");

                    break;

                case 6:

                    System.out.print("Enter Book ID to Delete: ");
                    int deleteId = sc.nextInt();

                    boolean removed = books.removeIf(book -> book.id == deleteId);

                    if (removed)
                        System.out.println("Book Deleted Successfully!");
                    else
                        System.out.println("Book Not Found!");

                    break;

                case 7:

                    System.out.println("Thank You!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");

            }
        }
    }
                      }
