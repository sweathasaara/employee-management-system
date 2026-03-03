package dao;

import java.sql.*;
import java.util.Scanner;

import util.DBConnection;

public class LeaveDAO {

    public void applyLeave(int empId, String reason) {
        try (Connection con = DBConnection.getConnection()) {

            String query = "INSERT INTO leaves(employee_id, reason, status) VALUES (?, ?, 'pending')";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, empId);
            ps.setString(2, reason);

            ps.executeUpdate();
            System.out.println("Leave applied!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
   public String viewPendingLeaves() {
    StringBuilder result = new StringBuilder();

    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT * FROM leaves WHERE status='pending'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        StringBuilder sb = new StringBuilder();

while(rs.next()) {
    sb.append(rs.getInt("id")).append(" - ")
      .append(rs.getInt("employee_id")).append(" - ")
      .append(rs.getString("reason")).append(" - ")
      .append(rs.getString("status")).append("\n");
}

return sb.toString();

    } catch (Exception e) {
        return e.toString();
    }


}
public String showAllLeaves() {

    StringBuilder sb = new StringBuilder();
    sb.append("[");

    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT l.id, e.name, l.reason, l.status " +
                       "FROM leaves l JOIN employees e ON l.employee_id = e.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        boolean first = true;

        while (rs.next()) {

            if (!first) sb.append(",");
            first = false;

            sb.append("{")
              .append("\"id\":").append(rs.getInt("id")).append(",")
              .append("\"employee\":\"").append(rs.getString("name")).append("\",")
              .append("\"reason\":\"").append(rs.getString("reason")).append("\",")
              .append("\"status\":\"").append(rs.getString("status")).append("\"")
              .append("}");
        }

    } catch (Exception e) {
        return "{\"error\":\"" + e.getMessage() + "\"}";
    }

    sb.append("]");
    return sb.toString();
} 
public void approveLeave(int id) {
    try (Connection con = DBConnection.getConnection()) {
        String query = "UPDATE leaves SET status='approved' WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Leave approved!");
    } catch (Exception e) {
        System.out.println(e);
    }
}

public void rejectLeave(int id) {
    try (Connection con = DBConnection.getConnection()) {
        String query = "UPDATE leaves SET status='rejected' WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Leave rejected!");
    } catch (Exception e) {
        System.out.println(e);
    }
}
}