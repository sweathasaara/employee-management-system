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
    public void viewPendingLeaves() {
    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT * FROM leaves WHERE status='pending'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("+----+----------+----------------------+-----------+");
        System.out.printf("| %-2s | %-8s | %-20s | %-9s |\n", "ID", "EmpID", "Reason", "Status");
        System.out.println("+----+----------+----------------------+-----------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-8d | %-20s | %-9s |\n",
                    rs.getInt("id"),
                    rs.getInt("employee_id"),
                    rs.getString("reason"),
                    rs.getString("status"));
        }

        System.out.println("+----+----------+----------------------+-----------+");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public void showAllLeaves() {
    try (Connection con = DBConnection.getConnection()) {

        String query = "SELECT l.id, e.name, l.reason, l.status " +
                       "FROM leaves l JOIN employees e ON l.employee_id = e.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("+----+----------------------+----------------------+-----------+");
        System.out.printf("| %-2s | %-20s | %-20s | %-9s |\n", "ID", "Name", "Reason", "Status");
        System.out.println("+----+----------------------+----------------------+-----------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-20s | %-20s | %-9s |\n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("reason"),
                    rs.getString("status"));
        }

        System.out.println("+----+----------------------+----------------------+-----------+");

    } catch (Exception e) {
        System.out.println(e);
    }
}
public void approveLeave(Scanner sc) {
    try (Connection con = DBConnection.getConnection()) {

        System.out.print("Enter Leave ID: ");
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
public void rejectLeave(Scanner sc) {
    try (Connection con = DBConnection.getConnection()) {

        System.out.print("Enter Leave ID: ");
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
}