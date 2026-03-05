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
public int authenticateEmployee(String username,String password){

    try(Connection con = DBConnection.getConnection()){

        String query = "SELECT id FROM employees WHERE username=? AND password=?";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1,username);
        ps.setString(2,password);

        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            return rs.getInt("id");
        }

    }catch(Exception e){
        System.out.println(e);
    }

    return -1;
}
public String showEmployees() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT e.id, e.name, d.name as dept, e.salary " +
                       "FROM employees e JOIN departments d ON e.department_id = d.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        boolean first = true;

        while (rs.next()) {
            if (!first) sb.append(",");
            first = false;

            sb.append("{")
              .append("\"id\":").append(rs.getInt("id")).append(",")
              .append("\"name\":\"").append(rs.getString("name")).append("\",")
              .append("\"department\":\"").append(rs.getString("dept")).append("\",")
              .append("\"salary\":").append(rs.getDouble("salary"))
              .append("}");
        }

    } catch (Exception e) {
        return "{\"error\":\"" + e.getMessage() + "\"}";
    }

    sb.append("]");
    return sb.toString();
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