package main;

import java.util.Scanner;
import service.EmployeeService;
import dao.*;

public class EmployeeApp {

    static Scanner sc = new Scanner(System.in);

    static EmployeeService empService = new EmployeeService();
    static DepartmentDAO deptDAO = new DepartmentDAO();
    static LeaveDAO leaveDAO = new LeaveDAO();
    static AttendanceDAO attDAO = new AttendanceDAO();
    static EmployeeDAO empDAO = new EmployeeDAO();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n--- Employee Management System ---");
System.out.println("1. Add Employee");
System.out.println("2. Show Employees");
System.out.println("3. Update Employee");
System.out.println("4. Delete Employee");
System.out.println("5. Add Department");
System.out.println("6. Delete Department");
System.out.println("7. Apply Leave");
System.out.println("8. View Pending Leaves");
System.out.println("9. Show All Leaves");
System.out.println("10. Approve Leave");
System.out.println("11. Reject Leave");
System.out.println("12. Mark Attendance");
System.out.println("13. View Attendance");
System.out.println("14. Exit");

            int choice = sc.nextInt();
           switch (choice) {

case 1:
    sc.nextLine();
    System.out.print("Enter Name: ");
    String name = sc.nextLine();
    System.out.print("Enter Salary: ");
    double salary = sc.nextDouble();

    deptDAO.showDepartments();
    System.out.print("Enter Dept ID: ");
    int deptId = sc.nextInt();

    empService.addEmployee(name, salary, deptId);
    break;

case 2:
    empDAO.showEmployees();
    break;

case 3:
    System.out.print("Enter Employee ID: ");
    int upId = sc.nextInt();
    sc.nextLine();
    System.out.print("Enter New Name: ");
    String newName = sc.nextLine();
    System.out.print("Enter New Salary: ");
    double newSalary = sc.nextDouble();

    deptDAO.showDepartments();
    System.out.print("Enter Dept ID: ");
    int newDept = sc.nextInt();

    empDAO.updateEmployee(upId, newName, newSalary, newDept);
    break;

case 4:
    System.out.print("Enter Employee ID: ");
    int delId = sc.nextInt();
    empDAO.deleteEmployee(delId);
    break;

case 5:
    sc.nextLine();
    System.out.print("Enter Department Name: ");
    deptDAO.addDepartment(sc.nextLine());
    break;

case 6:
    System.out.print("Enter Department ID: ");
    int dId = sc.nextInt();
    deptDAO.deleteDepartment(dId);
    break;

case 7:
    System.out.print("Enter Emp ID: ");
    int empId = sc.nextInt();
    sc.nextLine();
    System.out.print("Enter Reason: ");
    leaveDAO.applyLeave(empId, sc.nextLine());
    break;

case 8:
    leaveDAO.viewPendingLeaves();
    break;

case 9:
    leaveDAO.showAllLeaves();
    break;

case 10:
    leaveDAO.approveLeave(sc);
    break;

case 11:
    leaveDAO.rejectLeave(sc);
    break;

case 12:
    System.out.print("Enter Emp ID: ");
    int id = sc.nextInt();
    sc.nextLine();
    System.out.print("Enter Date: ");
    String date = sc.nextLine();
    System.out.print("Enter Status: ");
    String status = sc.nextLine();

    attDAO.markAttendance(id, date, status);
    break;

case 13:
    attDAO.viewAttendance();
    break;

case 14:
    System.out.println("Exit");
    return;
}
        }
    }
}