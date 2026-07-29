import java.util.ArrayList;
import java.util.Scanner;

class Employee {
    int id;
    String name;
    double basicSalary;

    Employee(int id, String name, double basicSalary) {
        this.id = id;
        this.name = name;
        this.basicSalary = basicSalary;
    }

    double calculateBonus() {
        return basicSalary * 0.10;
    }

    double calculateTax() {
        return basicSalary * 0.05;
    }

    double getNetSalary() {
        return basicSalary + calculateBonus() - calculateTax();
    }

    void displaySalarySlip() {
        System.out.println("\n========== SALARY SLIP ==========");
        System.out.println("Employee ID   : " + id);
        System.out.println("Employee Name : " + name);
        System.out.println("Basic Salary  : ₹" + basicSalary);
        System.out.println("Bonus (10%)   : ₹" + calculateBonus());
        System.out.println("Tax (5%)      : ₹" + calculateTax());
        System.out.println("---------------------------------");
        System.out.println("Net Salary    : ₹" + getNetSalary());
    }
}

public class EmployeePayrollSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Employee> employees = new ArrayList<>();

        while (true) {

            System.out.println("\n===== EMPLOYEE PAYROLL SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Generate Salary Slip");
            System.out.println("4. Exit");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Employee ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Employee Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Basic Salary: ");
                    double salary = sc.nextDouble();

                    employees.add(new Employee(id, name, salary));
                    System.out.println("Employee Added Successfully.");
                    break;

                case 2:
                    if (employees.isEmpty()) {
                        System.out.println("No Employees Found.");
                    } else {
                        System.out.println("\nEmployee List");
                        for (Employee e : employees) {
                            System.out.println(e.id + " - " + e.name);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Employee ID: ");
                    int searchId = sc.nextInt();

                    boolean found = false;

                    for (Employee e : employees) {
                        if (e.id == searchId) {
                            e.displaySalarySlip();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Employee Not Found.");
                    }
                    break;

                case 4:
                    System.out.println("Thank You!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
}
