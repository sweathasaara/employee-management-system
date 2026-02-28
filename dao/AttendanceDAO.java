package dao;

import java.sql.*;
import util.DBConnection;

public class AttendanceDAO {

    public void markAttendance(int empId, String date, String status) {
        try (Connection con = DBConnection.getConnection()) {

            String query = "INSERT INTO attendance(employee_id, date, status) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, empId);
            ps.setString(2, date);
            ps.setString(3, status);

            ps.executeUpdate();
            System.out.println("Attendance marked!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void viewAttendance() {
    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT a.id, e.name, a.date, a.status " +
                       "FROM attendance a JOIN employees e ON a.employee_id = e.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("+----+----------------------+------------+----------+");
        System.out.printf("| %-2s | %-20s | %-10s | %-8s |\n", "ID", "Name", "Date", "Status");
        System.out.println("+----+----------------------+------------+----------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-20s | %-10s | %-8s |\n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    rs.getString("status"));
        }

        System.out.println("+----+----------------------+------------+----------+");

    } catch (Exception e) {
        System.out.println(e);
    }
}
}