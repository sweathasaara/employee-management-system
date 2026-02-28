package service;

import dao.EmployeeDAO;
import model.Employee;

public class EmployeeService {

    EmployeeDAO dao = new EmployeeDAO();

    public void addEmployee(String name, double salary, int deptId) {

        Employee emp = new Employee();
        emp.name = name;
        emp.salary = salary;
        emp.departmentId = deptId;

        dao.addEmployee(emp);
    }
}