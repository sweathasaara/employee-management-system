package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;

import service.EmployeeService;

public class SimpleServer {

    static EmployeeService service = new EmployeeService();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 1 ADD EMPLOYEE
        server.createContext("/employee/add", e -> {
            service.addEmployee("Sara", 40000, 101);
            send(e, "Employee Added");
        });

        // 2 SHOW EMPLOYEES
        server.createContext("/employee/show", e -> {
            send(e, service.showEmployees());
        });

        // 3 UPDATE
        server.createContext("/employee/update", e -> {
            service.updateEmployee(1, "Updated", 50000, 102);
            send(e, "Employee Updated");
        });

        // 4 DELETE
        server.createContext("/employee/delete", e -> {
            service.deleteEmployee(1);
            send(e, "Employee Deleted");
        });

        // 5 ADD DEPT
        server.createContext("/department/add", e -> {
            service.addDepartment("New Dept");
            send(e, "Department Added");
        });

        // 6 DELETE DEPT
        server.createContext("/department/delete", e -> {
            service.deleteDepartment(101);
            send(e, "Department Deleted");
        });

        // 7 APPLY LEAVE
        server.createContext("/leave/apply", e -> {
            service.applyLeave(1, "Sick Leave");
            send(e, "Leave Applied");
        });

        // 8 VIEW PENDING
       server.createContext("/leave/pending", e -> {
            send(e, service.showEmployees());
        });


        // 9 SHOW LEAVES
        server.createContext("/leave/show", e -> {
            send(e, service.showAllLeaves());
        });

        // 10 APPROVE
        server.createContext("/leave/approve", e -> {
            service.approveLeave(1);
            send(e, "Leave Approved");
        });

        // 11 REJECT
        server.createContext("/leave/reject", e -> {
            service.rejectLeave(2);
            send(e, "Leave Rejected");
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }

    static void send(HttpExchange e, String res) throws IOException {
        e.getResponseHeaders().add("Content-Type", "text/plain");
        e.sendResponseHeaders(200, res.length());
        OutputStream os = e.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }
}