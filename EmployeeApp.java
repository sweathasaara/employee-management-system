import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {

    static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    static final String USER = "root";
    static final String PASSWORD = "123456";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Show Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Add Department");
            System.out.println("6. Delete Department");
            System.out.println("7. Apply Leave");
            System.out.println("8. View Pending Leaves");
            System.out.println("9. Show Leaves");
            System.out.println("10. Approve Leave");
            System.out.println("11. Reject Leave");
            System.out.println("12. Exit");
            System.out.print("Choose option: ");
            

            int choice = sc.nextInt();

            switch (choice) {
    case 1: addEmployee(); break;
    case 2: showEmployees(); break;
    case 3: updateEmployee(); break;
    case 4: deleteEmployee(); break;
    case 5: addDepartment(); break;
    case 6: deleteDepartment(); break;
    case 7: applyLeave(); break;
    case 8: viewPendingLeaves(); break;
    case 9: showAllLeaves(); break;
    case 10: approveLeave(); break;
    case 11: rejectLeave(); break;
    case 12:
        System.out.println("Exiting...");
        return;
    default:
        System.out.println("Invalid choice!");
}
        }
    }

    public static void addEmployee() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        showDepartments();

        System.out.print("Enter Department ID: ");
        int deptId = sc.nextInt();

        String query = "INSERT INTO employees(name, salary, department_id) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);
        ps.setDouble(2, salary);
        ps.setInt(3, deptId);

        ps.executeUpdate();

        System.out.println("Employee added successfully!");

    } catch (Exception e) {
        System.out.println(e);
    }
}


    public static void showEmployees() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String query = "SELECT e.id, e.name, d.name AS department, e.salary " +
               "FROM employees e JOIN departments d ON e.department_id = d.id";
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
    public static void applyLeave() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        System.out.print("Enter Employee ID: ");
        int empId = sc.nextInt();

        sc.nextLine();
        System.out.print("Enter Reason: ");
        String reason = sc.nextLine();

        String query = "INSERT INTO leaves(employee_id, reason, status) VALUES (?, ?, 'pending')";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, empId);
        ps.setString(2, reason);

        ps.executeUpdate();

        System.out.println("Leave applied successfully!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void viewPendingLeaves() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        String query = "SELECT * FROM leaves WHERE status='pending'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("+----+----------+------------------------------+-----------+");
System.out.printf("| %-2s | %-8s | %-28s | %-9s |\n", "ID", "EmpID", "Reason", "Status");
System.out.println("+----+----------+------------------------------+-----------+");

while (rs.next()) {
    System.out.printf("| %-2d | %-8d | %-28s | %-9s |\n",
            rs.getInt("id"),
            rs.getInt("employee_id"),
            rs.getString("reason"),
            rs.getString("status"));
}

System.out.println("+----+----------+------------------------------+-----------+");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void showAllLeaves() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        String query = "SELECT l.id, e.name, l.reason, l.status " +
                       "FROM leaves l JOIN employees e ON l.employee_id = e.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("+----+----------------------+------------------------------+-----------+");
        System.out.printf("| %-2s | %-20s | %-28s | %-9s |\n", "ID", "Employee", "Reason", "Status");
        System.out.println("+----+----------------------+------------------------------+-----------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-20s | %-28s | %-9s |\n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("reason"),
                    rs.getString("status"));
        }

        System.out.println("+----+----------------------+------------------------------+-----------+");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void approveLeave() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        System.out.print("Enter Leave ID to approve: ");
        int id = sc.nextInt();

        String query = "UPDATE leaves SET status='approved' WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        ps.executeUpdate();
        System.out.println("Leave approved!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void rejectLeave() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        System.out.print("Enter Leave ID to reject: ");
        int id = sc.nextInt();

        String query = "UPDATE leaves SET status='rejected' WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        ps.executeUpdate();
        System.out.println("Leave rejected!");

    } catch (Exception e) {
        System.out.println(e);
    }
}

    public static void updateEmployee() {
    try {
       
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
        sc.nextLine(); 

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
    showDepartments();
    System.out.print("Enter New Department ID: ");
    int deptId = sc.nextInt();

    query = "UPDATE employees SET department_id=? WHERE id=?";
    ps = con.prepareStatement(query);
    ps.setInt(1, deptId);
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

    System.out.print("Enter New Salary: ");
    double newSalary = sc.nextDouble();

    showDepartments();
    System.out.print("Enter Department ID: ");
    int newDeptId = sc.nextInt();

    query = "UPDATE employees SET name=?, salary=?, department_id=? WHERE id=?";
    ps = con.prepareStatement(query);
    ps.setString(1, newName);
    ps.setDouble(2, newSalary);
    ps.setInt(3, newDeptId);
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
    public static void addDepartment() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
        sc.nextLine();
        System.out.print("Enter Department Name: ");
        String name = sc.nextLine();

        String query = "INSERT INTO departments(name) VALUES (?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);

        ps.executeUpdate();
        System.out.println("Department added successfully!");

    } catch (Exception e) {
        System.out.println(e);
    }
    
}
public static void showDepartments() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        String query = "SELECT * FROM departments";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("+----+--------------------------+");
        System.out.printf("| %-2s | %-24s |\n", "ID", "Department Name");
        System.out.println("+----+--------------------------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-24s |\n",
                    rs.getInt("id"),
                    rs.getString("name"));
        }

        System.out.println("+----+--------------------------+");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void deleteDepartment() {
    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

        System.out.print("Enter Department ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM departments WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();

        if (rows > 0)
            System.out.println("Department deleted!");
        else
            System.out.println("Department not found!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
}