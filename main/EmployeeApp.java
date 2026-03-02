package main;

import java.util.Scanner;
import dao.EmployeeDAO;
import dao.DepartmentDAO;
import dao.LeaveDAO;

public class EmployeeApp {

    static Scanner sc = new Scanner(System.in);

    static EmployeeDAO empDAO = new EmployeeDAO();
    static DepartmentDAO deptDAO = new DepartmentDAO();
    static LeaveDAO leaveDAO = new LeaveDAO();

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
            System.out.println("9. Show Leaves");
            System.out.println("10. Approve Leave");
            System.out.println("11. Reject Leave");
            System.out.println("12. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Name: ");
                    sc.nextLine();
                    String name = sc.nextLine();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();

                    System.out.print("Enter Department ID: ");
                    int deptId = sc.nextInt();

                    empDAO.addEmployee(name, salary, deptId);
                    break;

                case 2:
                    empDAO.showEmployees();
                    break;

                case 3:
                    System.out.print("Enter Employee ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter New Name: ");
                    sc.nextLine();
                    String newName = sc.nextLine();

                    System.out.print("Enter New Salary: ");
                    double newSalary = sc.nextDouble();

                    System.out.print("Enter Department ID: ");
                    int newDeptId = sc.nextInt();

                    empDAO.updateEmployee(id, newName, newSalary, newDeptId);
                    break;

                case 4:
                    System.out.print("Enter Employee ID: ");
                    int delId = sc.nextInt();
                    empDAO.deleteEmployee(delId);
                    break;

                case 5:
                    System.out.print("Enter Department Name: ");
                    sc.nextLine();
                    String deptName = sc.nextLine();
                    deptDAO.addDepartment(deptName);
                    break;

                case 6:
                    System.out.print("Enter Department ID: ");
                    int deptDel = sc.nextInt();
                    deptDAO.deleteDepartment(deptDel);
                    break;

                case 7:
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();

                    sc.nextLine();
                    System.out.print("Enter Reason: ");
                    String reason = sc.nextLine();

                    leaveDAO.applyLeave(empId, reason);
                    break;

                case 8:
                    leaveDAO.viewPendingLeaves();
                    break;

                case 9:
                    leaveDAO.showAllLeaves();
                    break;

                case 10:
                    System.out.print("Enter Leave ID to approve: ");
                    int approveId = sc.nextInt();
                    leaveDAO.approveLeave(approveId);   // ✅ FIXED
                    break;

                case 11:
                    System.out.print("Enter Leave ID to reject: ");
                    int rejectId = sc.nextInt();
                    leaveDAO.rejectLeave(rejectId);     // ✅ FIXED
                    break;

                case 12:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}