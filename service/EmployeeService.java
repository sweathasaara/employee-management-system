package service;

import dao.EmployeeDAO;
import dao.DepartmentDAO;
import dao.LeaveDAO;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.AttendanceDAO;



public class EmployeeService {

    EmployeeDAO empDAO = new EmployeeDAO();
    DepartmentDAO deptDAO = new DepartmentDAO();
    LeaveDAO leaveDAO = new LeaveDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

public int authenticateEmployee(String username,String password){

try(Connection con = DBConnection.getConnection()){

String query =
"SELECT employee_id FROM login_details WHERE username=? AND password_hash=SHA2(?,256)";

PreparedStatement ps = con.prepareStatement(query);

ps.setString(1,username);
ps.setString(2,password);

ResultSet rs = ps.executeQuery();

if(rs.next()){
return rs.getInt("employee_id");
}

}catch(Exception e){
System.out.println(e);
}

return -1;
}
    public void addEmployee(String name, double salary, int deptId) {
        empDAO.addEmployee(name, salary, deptId);
    }
   public String showEmployees() {
    return empDAO.showEmployees();
}
    public void updateEmployee(int id, String name, double salary, int deptId) {
        empDAO.updateEmployee(id, name, salary, deptId);
    }

    public void deleteEmployee(int id) {
        empDAO.deleteEmployee(id);
    }

    // DEPARTMENT
    public void addDepartment(String name) {
        deptDAO.addDepartment(name);
    }

    public void deleteDepartment(int id) {
        deptDAO.deleteDepartment(id);
    }
    public void applyLeave(int empId, String reason) {
        leaveDAO.applyLeave(empId, reason);
    }

   public String viewPendingLeaves() {
    return leaveDAO.viewPendingLeaves();
}
    public String showAllLeaves() {
    return leaveDAO.showAllLeaves();
}
public String showDepartments() {
    return deptDAO.showDepartments();
}

    public void approveLeave(int id) {
        leaveDAO.approveLeave(id);
    }

    public void rejectLeave(int id) {
        leaveDAO.rejectLeave(id);
    }
    public void markAttendance(int empId, String date, String status) {
    attendanceDAO.markAttendance(empId, date, status);
}


public String viewAttendance() {
    return attendanceDAO.viewAttendance();
}
}