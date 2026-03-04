package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import service.EmployeeService;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SimpleServer {

    static EmployeeService service = new EmployeeService();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/employees", SimpleServer::handleEmployees);
        server.createContext("/departments", SimpleServer::handleDepartments);
        server.createContext("/leaves", SimpleServer::handleLeaves);
        server.createContext("/attendance", SimpleServer::handleAttendance);

        server.start();

        System.out.println("Server started at http://localhost:8080");
    }



    static void handleEmployees(HttpExchange exchange) throws IOException {

        String role = authenticate(exchange);
        if(role == null) return;

        if(!role.equals("admin") && !role.equals("manager")) {
            send(exchange,403,"{\"error\":\"Forbidden\"}");
            return;
        }

        String method = exchange.getRequestMethod();

        if(method.equals("GET")) {
            send(exchange,200,service.showEmployees());
        }

        else if(method.equals("POST")) {

            if(!role.equals("admin")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            String body = readBody(exchange);
            Map<String,String> json = parseJson(body);

            service.addEmployee(
                    json.get("name"),
                    Double.parseDouble(json.get("salary")),
                    Integer.parseInt(json.get("deptId"))
            );

            send(exchange,201,"{\"message\":\"Employee created\"}");
        }

        else if(method.equals("PUT")) {

            if(!role.equals("admin")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            String body = readBody(exchange);
            Map<String,String> json = parseJson(body);

            service.updateEmployee(
                    Integer.parseInt(json.get("id")),
                    json.get("name"),
                    Double.parseDouble(json.get("salary")),
                    Integer.parseInt(json.get("deptId"))
            );

            send(exchange,200,"{\"message\":\"Employee updated\"}");
        }

        else if(method.equals("DELETE")) {

            if(!role.equals("admin")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            Map<String,String> query = parseQuery(exchange.getRequestURI().getQuery());

            service.deleteEmployee(Integer.parseInt(query.get("id")));

            send(exchange,200,"{\"message\":\"Employee deleted\"}");
        }
    }



    static void handleDepartments(HttpExchange exchange) throws IOException {

        String role = authenticate(exchange);
        if(role == null) return;

        if(!role.equals("admin")){
            send(exchange,403,"{\"error\":\"Forbidden\"}");
            return;
        }

        String method = exchange.getRequestMethod();

        if(method.equals("POST")) {

            String body = readBody(exchange);
            Map<String,String> json = parseJson(body);

            service.addDepartment(json.get("name"));

            send(exchange,201,"{\"message\":\"Department created\"}");
        }

        else if(method.equals("DELETE")) {

            Map<String,String> query = parseQuery(exchange.getRequestURI().getQuery());

            service.deleteDepartment(Integer.parseInt(query.get("id")));

            send(exchange,200,"{\"message\":\"Department deleted\"}");
        }
    }



    static void handleLeaves(HttpExchange exchange) throws IOException {

        String role = authenticate(exchange);
        if(role == null) return;

        String method = exchange.getRequestMethod();

        if(method.equals("GET")) {

            if(role.equals("employee")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            send(exchange,200,service.showAllLeaves());
        }

        else if(method.equals("POST")) {

            if(!role.equals("employee")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            String body = readBody(exchange);
            Map<String,String> json = parseJson(body);

            service.applyLeave(
                    Integer.parseInt(json.get("empId")),
                    json.get("reason")
            );

            send(exchange,201,"{\"message\":\"Leave applied\"}");
        }

        else if(method.equals("PUT")) {

            if(!role.equals("manager") && !role.equals("admin")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            Map<String,String> query = parseQuery(exchange.getRequestURI().getQuery());

            int id = Integer.parseInt(query.get("id"));
            String status = query.get("status");

            if(status.equals("approved"))
                service.approveLeave(id);
            else
                service.rejectLeave(id);

            send(exchange,200,"{\"message\":\"Leave updated\"}");
        }
    }


    static void handleAttendance(HttpExchange exchange) throws IOException {

        String role = authenticate(exchange);
        if(role == null) return;

        String method = exchange.getRequestMethod();

        if(method.equals("GET")) {

            if(role.equals("employee")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            send(exchange,200,service.viewAttendance());
        }

        else if(method.equals("POST")) {

            if(!role.equals("admin")){
                send(exchange,403,"{\"error\":\"Forbidden\"}");
                return;
            }

            String body = readBody(exchange);
            Map<String,String> json = parseJson(body);

            service.markAttendance(
                    Integer.parseInt(json.get("empId")),
                    json.get("date"),
                    json.get("status")
            );

            send(exchange,201,"{\"message\":\"Attendance marked\"}");
        }
    }



    static String authenticate(HttpExchange exchange) throws IOException {

        String auth = exchange.getRequestHeaders().getFirst("Authorization");

        if(auth == null || !auth.startsWith("Basic ")){

            exchange.getResponseHeaders().add("WWW-Authenticate","Basic");
            send(exchange,401,"{\"error\":\"Unauthorized\"}");
            return null;
        }

        String base64 = auth.substring(6);
        String decoded = new String(Base64.getDecoder().decode(base64));

        String[] parts = decoded.split(":");

        String user = parts[0];
        String pass = parts[1];

        if(user.equals("admin") && pass.equals("password"))
            return "admin";

        if(user.equals("manager") && pass.equals("password"))
            return "manager";

        if(user.equals("employee") && pass.equals("password"))
            return "employee";

        send(exchange,401,"{\"error\":\"Invalid login\"}");
        return null;
    }



    static void send(HttpExchange exchange,int status,String response) throws IOException {

        exchange.getResponseHeaders().add("Content-Type","application/json");

        exchange.sendResponseHeaders(status,response.getBytes().length);

        OutputStream os = exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }

    static String readBody(HttpExchange exchange) throws IOException {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(),StandardCharsets.UTF_8)
        );

        StringBuilder sb = new StringBuilder();

        String line;

        while((line=br.readLine())!=null)
            sb.append(line);

        return sb.toString();
    }

    static Map<String,String> parseQuery(String query){

        Map<String,String> map = new HashMap<>();

        if(query == null)
            return map;

        for(String param : query.split("&")){

            String[] pair = param.split("=");

            map.put(pair[0], URLDecoder.decode(pair[1],StandardCharsets.UTF_8));
        }

        return map;
    }

    static Map<String,String> parseJson(String json){

        Map<String,String> map = new HashMap<>();

        json = json.replaceAll("[{}\"]","");

        for(String pair : json.split(",")){

            String[] kv = pair.split(":");

            map.put(kv[0].trim(),kv[1].trim());
        }

        return map;
    }
}