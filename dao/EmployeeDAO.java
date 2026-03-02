package dao;

import java.sql.*;
import util.DBConnection;

public class EmployeeDAO {

    public void addEmployee(String name, double salary, int deptId) {
    try (Connection con = DBConnection.getConnection()) {
        String query = "INSERT INTO employees(name, salary, department_id) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);
        ps.setDouble(2, salary);
        ps.setInt(3, deptId);

        ps.executeUpdate();
        System.out.println("Employee added!");
    } catch (Exception e) {
        System.out.println(e);
    }
}

    public String showEmployees() {
    StringBuilder result = new StringBuilder();

    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT e.id, e.name, d.name AS department, e.salary " +
                       "FROM employees e JOIN departments d ON e.department_id = d.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            result.append("ID: ").append(rs.getInt("id"))
                  .append(" Name: ").append(rs.getString("name"))
                  .append(" Dept: ").append(rs.getString("department"))
                  .append(" Salary: ").append(rs.getDouble("salary"))
                  .append("\n");
        }

    } catch (Exception e) {
        return e.toString();
    }

    return result.toString();
}
    public void updateEmployee(int id, String name, double salary, int deptId) {
    try (Connection con = DBConnection.getConnection()) {
        String query = "UPDATE employees SET name=?, salary=?, department_id=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);
        ps.setDouble(2, salary);
        ps.setInt(3, deptId);
        ps.setInt(4, id);

        ps.executeUpdate();
        System.out.println("Employee updated!");
    } catch (Exception e) {
        System.out.println(e);
    }

}
public void deleteEmployee(int id) {
    try (Connection con = DBConnection.getConnection()) {
        String query = "DELETE FROM employees WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        ps.executeUpdate();
        System.out.println("Employee deleted!");
    } catch (Exception e) {
        System.out.println(e);
    }
}
}