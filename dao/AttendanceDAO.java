package dao;

import java.sql.*;
import util.DBConnection;
import dao.AttendanceDAO;
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
  public String viewAttendance() {

    StringBuilder sb = new StringBuilder();
    sb.append("[");

    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT a.id, e.name, a.date, a.status " +
                       "FROM attendance a JOIN employees e ON a.employee_id = e.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        boolean first = true;

        while (rs.next()) {

            if (!first) sb.append(",");
            first = false;

            sb.append("{")
              .append("\"id\":").append(rs.getInt("id")).append(",")
              .append("\"employee\":\"").append(rs.getString("name")).append("\",")
              .append("\"date\":\"").append(rs.getString("date")).append("\",")
              .append("\"status\":\"").append(rs.getString("status")).append("\"")
              .append("}");
        }

    } catch (Exception e) {
        return "{\"error\":\"" + e.getMessage() + "\"}";
    }

    sb.append("]");
    return sb.toString();
}
}