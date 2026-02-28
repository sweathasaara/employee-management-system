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

    public void showDepartments() {
        try (Connection con = DBConnection.getConnection()) {

            String query = "SELECT * FROM departments";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("+----+--------------------------+");
            System.out.printf("| %-3s | %-24s |\n", "ID", "Department");
            System.out.println("+----+--------------------------+");

            while (rs.next()) {
                System.out.printf("| %-3d | %-24s |\n",
                        rs.getInt("id"),
                        rs.getString("name"));
            }

            System.out.println("+----+--------------------------+");

        } catch (Exception e) {
            System.out.println(e);
        }
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