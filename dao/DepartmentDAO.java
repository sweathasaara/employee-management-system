package dao;

import java.sql.*;
import util.DBConnection;

public class DepartmentDAO {

    public void addDepartment(String name) {
        try (Connection con = DBConnection.getConnection()) {

            String query = "INSERT INTO departments(name) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);

            ps.executeUpdate();
            System.out.println("Department added!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

   public String showDepartments() {
    StringBuilder result = new StringBuilder();

    try (Connection con = DBConnection.getConnection()) {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM departments");

        result.append("Departments:\n");

        while (rs.next()) {
            result.append(rs.getInt("id") + " - " + rs.getString("name") + "\n");
        }

    } catch (Exception e) {
        return e.toString();
    }

    return result.toString();
}
    public void deleteDepartment(int id) {
    try (Connection con = DBConnection.getConnection()) {

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