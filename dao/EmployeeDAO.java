package dao;

import java.sql.*;
import model.Employee;
import util.DBConnection;

public class EmployeeDAO {

    public void addEmployee(Employee emp) {
        try (Connection con = DBConnection.getConnection()) {

            String query = "INSERT INTO employees(name, salary, department_id) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, emp.name);
            ps.setDouble(2, emp.salary);
            ps.setInt(3, emp.departmentId);

            ps.executeUpdate();
            System.out.println("Employee added!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showEmployees() {
        try (Connection con = DBConnection.getConnection()) {

            String query = "SELECT e.id, e.name, d.name AS department, e.salary " +
                    "FROM employees e JOIN departments d ON e.department_id = d.id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("+----+----------------------+------------------------------+-----------+");
            System.out.printf("| %-2s | %-20s | %-28s | %-9s |\n", "ID", "Name", "Department", "Salary");
            System.out.println("+----+----------------------+------------------------------+-----------+");

            while (rs.next()) {
                System.out.printf("| %-2d | %-20s | %-28s | %-9.1f |\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }

            System.out.println("+----+----------------------+------------------------------+-----------+");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void updateEmployee(int id, String name, double salary, int deptId) {
    try (Connection con = DBConnection.getConnection()) {

        String query = "UPDATE employees SET name=?, salary=?, department_id=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);
        ps.setDouble(2, salary);
        ps.setInt(3, deptId);
        ps.setInt(4, id);

        int rows = ps.executeUpdate();

        if (rows > 0)
            System.out.println("Employee updated!");
        else
            System.out.println("Employee not found!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public void deleteEmployee(int id) {
    try (Connection con = DBConnection.getConnection()) {

        String query = "DELETE FROM employees WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();

        if (rows > 0)
            System.out.println("Employee deleted!");
        else
            System.out.println("Employee not found!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
}