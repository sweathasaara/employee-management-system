package service;

import dao.EmployeeDAO;
import dao.DepartmentDAO;
import dao.LeaveDAO;

public class EmployeeService {

    EmployeeDAO empDAO = new EmployeeDAO();
    DepartmentDAO deptDAO = new DepartmentDAO();
    LeaveDAO leaveDAO = new LeaveDAO();


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

    // LEAVE
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
}