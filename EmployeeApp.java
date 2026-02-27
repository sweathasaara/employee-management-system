import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {

    static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    static final String USER = "root";
    static final String PASSWORD = "123456";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\nEmployee Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. Show Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    showEmployees();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // CREATE
    public static void addEmployee() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.print("Enter Name: ");
            sc.nextLine();
            String name = sc.nextLine();

            System.out.print("Enter Department: ");
            String dept = sc.nextLine();

            System.out.print("Enter Salary: ");
            double salary = sc.nextDouble();

            String query = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);

            ps.executeUpdate();

            System.out.println("Employee added successfully!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // READ
    public static void showEmployees() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String query = "SELECT * FROM employees";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            System.out.println("+----+----------------------+----------------------------------------------+-----------+");
System.out.printf("| %-2s | %-20s | %-44s | %-9s |\n", "ID", "Name", "Department", "Salary");
System.out.println("+----+----------------------+----------------------------------------------+-----------+");

while (rs.next()) {
    System.out.printf("| %-2d | %-20s | %-44s | %-9.1f |\n",
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("department"),
            rs.getDouble("salary"));
}

System.out.println("+----+----------------------+----------------------------------------------+-----------+");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // UPDATE
    public static void updateEmployee() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

        System.out.print("Enter Employee ID to update: ");
        int id = sc.nextInt();

        System.out.println("\nWhat do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Department");
        System.out.println("3. Salary");
        System.out.println("4. All");
        System.out.print("Choose option: ");

        int choice = sc.nextInt();
        sc.nextLine(); // clear buffer

        String query = "";
        PreparedStatement ps = null;

        switch (choice) {

            case 1:
                System.out.print("Enter New Name: ");
                String name = sc.nextLine();

                query = "UPDATE employees SET name=? WHERE id=?";
                ps = con.prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, id);
                break;

            case 2:
                System.out.print("Enter New Department: ");
                String dept = sc.nextLine();

                query = "UPDATE employees SET department=? WHERE id=?";
                ps = con.prepareStatement(query);
                ps.setString(1, dept);
                ps.setInt(2, id);
                break;

            case 3:
                System.out.print("Enter New Salary: ");
                double salary = sc.nextDouble();

                query = "UPDATE employees SET salary=? WHERE id=?";
                ps = con.prepareStatement(query);
                ps.setDouble(1, salary);
                ps.setInt(2, id);
                break;

            case 4:
                System.out.print("Enter New Name: ");
                String newName = sc.nextLine();

                System.out.print("Enter New Department: ");
                String newDept = sc.nextLine();

                System.out.print("Enter New Salary: ");
                double newSalary = sc.nextDouble();

                query = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
                ps = con.prepareStatement(query);
                ps.setString(1, newName);
                ps.setString(2, newDept);
                ps.setDouble(3, newSalary);
                ps.setInt(4, id);
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        int rows = ps.executeUpdate();

        if (rows > 0)
            System.out.println("Employee updated successfully!");
        else
            System.out.println("Employee not found!");

        con.close();

    } catch (Exception e) {
        System.out.println(e);
    }
}
    // DELETE
    public static void deleteEmployee() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.print("Enter Employee ID to delete: ");
            int id = sc.nextInt();

            String query = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Employee deleted successfully!");
            else
                System.out.println("Employee not found!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}