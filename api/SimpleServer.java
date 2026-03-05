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
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
        server.createContext("/employees",SimpleServer::handleEmployees);
        server.createContext("/departments",SimpleServer::handleDepartments);
        server.createContext("/leaves",SimpleServer::handleLeaves);
        server.createContext("/attendance",SimpleServer::handleAttendance);
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
    static void handleEmployees(HttpExchange exchange) throws IOException {
        String role = authenticate(exchange);
        if(role==null) return;
        String method = exchange.getRequestMethod();
        if(!role.equals("admin") && !role.equals("manager")){
            send(exchange,403,"{\"error\":\"Only admin or manager can access employees\"}");
            return;
        }
        try{
            if(method.equals("GET")){
                send(exchange,200,service.showEmployees());
            }
            else if(method.equals("POST")){

                if(!role.equals("admin")){
                    send(exchange,403,"{\"error\":\"Only admin can add employees\"}");
                    return;
                }
                Map<String,String> json = parseJson(readBody(exchange));
                if(!json.containsKey("name") || !json.containsKey("salary") || !json.containsKey("deptId")){
                    send(exchange,400,"{\"error\":\"Missing required fields: name, salary, deptId\"}");
                    return;
                }
                service.addEmployee(
                        json.get("name"),
                        Double.parseDouble(json.get("salary")),
                        Integer.parseInt(json.get("deptId"))
                );
                send(exchange,201,"{\"message\":\"Employee created\"}");
            }
            else if(method.equals("PUT")){
                if(!role.equals("admin")){
                    send(exchange,403,"{\"error\":\"Only admin can update employees\"}");
                    return;
                }
                Map<String,String> json = parseJson(readBody(exchange));
                if(!json.containsKey("id")){
                    send(exchange,400,"{\"error\":\"Employee id required\"}");
                    return;
                }
                service.updateEmployee(
                        Integer.parseInt(json.get("id")),
                        json.get("name"),
                        Double.parseDouble(json.get("salary")),
                        Integer.parseInt(json.get("deptId"))
                );
                send(exchange,200,"{\"message\":\"Employee updated\"}");
            }
            else if(method.equals("DELETE")){
                if(!role.equals("admin")){
                    send(exchange,403,"{\"error\":\"Only admin can delete employees\"}");
                    return;
                }
                Map<String,String> query = parseQuery(exchange.getRequestURI().getQuery());
                if(!query.containsKey("id")){
                    send(exchange,400,"{\"error\":\"Employee id query parameter required\"}");
                    return;
                }
                service.deleteEmployee(Integer.parseInt(query.get("id")));
                send(exchange,200,"{\"message\":\"Employee deleted\"}");
            }
            else{
                send(exchange,405,"{\"error\":\"Method not allowed\"}");
            }
        }catch(Exception e){
            send(exchange,500,"{\"error\":\"Server error\"}");
        }
    }
    static void handleDepartments(HttpExchange exchange) throws IOException {
        String role = authenticate(exchange);
        if(role==null) return;
        if(!role.equals("admin")){
            send(exchange,403,"{\"error\":\"Only admin can manage departments\"}");
            return;
        }
        String method = exchange.getRequestMethod();
        try{
            if(method.equals("POST")){
                Map<String,String> json = parseJson(readBody(exchange));
                if(!json.containsKey("name")){
                    send(exchange,400,"{\"error\":\"Department name required\"}");
                    return;
                }
                service.addDepartment(json.get("name"));
                send(exchange,201,"{\"message\":\"Department created\"}");
            }
            else if(method.equals("DELETE")){
                Map<String,String> query = parseQuery(exchange.getRequestURI().getQuery());
                if(!query.containsKey("id")){
                    send(exchange,400,"{\"error\":\"Department id required\"}");
                    return;
                }
                service.deleteDepartment(Integer.parseInt(query.get("id")));
                send(exchange,200,"{\"message\":\"Department deleted\"}");
            }
            else{
                send(exchange,405,"{\"error\":\"Method not allowed\"}");
            }
        }catch(Exception e){
            send(exchange,500,"{\"error\":\"Server error\"}");
        }
    }
    static void handleLeaves(HttpExchange exchange) throws IOException {
        String role = authenticate(exchange);
        if(role==null) return;
        String method = exchange.getRequestMethod();
        try{
            if(method.equals("GET")){
                if(role.startsWith("employee")){
                    send(exchange,403,"{\"error\":\"Employees cannot view all leaves\"}");
                    return;
                }
                send(exchange,200,service.showAllLeaves());
            }
            else if(method.equals("POST")){
                if(!role.startsWith("employee")){
                    send(exchange,403,"{\"error\":\"Only employees can apply leave\"}");
                    return;
                }
                Map<String,String> json = parseJson(readBody(exchange));
       if(json.get("reason") == null || json.get("reason").isEmpty()){
    send(exchange,400,"{\"error\":\"Leave reason required\"}");
    return;
}
                int empId = Integer.parseInt(role.split(":")[1]);
                service.applyLeave(empId,json.get("reason"));
                send(exchange,201,"{\"message\":\"Leave applied\"}");
            }
            else if(method.equals("PUT")){
                if(!role.equals("manager") && !role.equals("admin")){
                    send(exchange,403,"{\"error\":\"Only manager or admin can approve leaves\"}");
                    return;
                }
                Map<String,String> query = parseQuery(exchange.getRequestURI().getQuery());
                if(!query.containsKey("id") || !query.containsKey("status")){
                    send(exchange,400,"{\"error\":\"Leave id and status required\"}");
                    return;
                }
                int id = Integer.parseInt(query.get("id"));
                String status = query.get("status");

                if(status.equals("approved"))
                    service.approveLeave(id);
                else if(status.equals("rejected"))
                    service.rejectLeave(id);
                else{
                    send(exchange,400,"{\"error\":\"Status must be approved or rejected\"}");
                    return;
                }

                send(exchange,200,"{\"message\":\"Leave updated\"}");
            }

            else{
                send(exchange,405,"{\"error\":\"Method not allowed\"}");
            }

        }catch(Exception e){
            send(exchange,500,"{\"error\":\"Server error\"}");
        }
    }
    static void handleAttendance(HttpExchange exchange) throws IOException {
        String role = authenticate(exchange);
        if(role==null) return;
        String method = exchange.getRequestMethod();
        try{
            if(method.equals("GET")){
                if(role.startsWith("employee")){
                    send(exchange,403,"{\"error\":\"Employees cannot view attendance\"}");
                    return;
                }
                send(exchange,200,service.viewAttendance());
            }
            else if(method.equals("POST")){

                if(!role.equals("admin")){
                    send(exchange,403,"{\"error\":\"Only admin can mark attendance\"}");
                    return;
                }
                Map<String,String> json = parseJson(readBody(exchange));
                if(!json.containsKey("empId") || !json.containsKey("date") || !json.containsKey("status")){
                    send(exchange,400,"{\"error\":\"empId, date and status required\"}");
                    return;
                }
                service.markAttendance(
                        Integer.parseInt(json.get("empId")),
                        json.get("date"),
                        json.get("status")
                );
                send(exchange,201,"{\"message\":\"Attendance marked\"}");
            }
            else{
                send(exchange,405,"{\"error\":\"Method not allowed\"}");
            }
        }catch(Exception e){
            send(exchange,500,"{\"error\":\"Server error\"}");
        }
    }
    static String authenticate(HttpExchange exchange) throws IOException {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if(auth==null || !auth.startsWith("Basic ")){
            exchange.getResponseHeaders().add("WWW-Authenticate","Basic");
            send(exchange,401,"{\"error\":\"Authentication required\"}");
            return null;
        }
        try{
            String decoded = new String(Base64.getDecoder().decode(auth.substring(6)));
            String[] parts = decoded.split(":");
            String user = parts[0];
            String pass = parts[1];
            if(user.equals("admin") && pass.equals("password"))
                return "admin";
            if(user.equals("manager") && pass.equals("password"))
                return "manager";
            int empId = service.authenticateEmployee(user,pass);
            if(empId!=-1){
                return "employee:"+empId;
            }
            send(exchange,401,"{\"error\":\"Invalid username or password\"}");
            return null;
        }catch(Exception e){
            send(exchange,400,"{\"error\":\"Invalid authentication format\"}");
            return null;
        }
    }
    static void send(HttpExchange exchange,int status,String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type","application/json");
        exchange.sendResponseHeaders(status,response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    static String readBody(HttpExchange exchange) throws IOException{
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
        if(query==null)
            return map;
        for(String param:query.split("&")){
            String[] pair = param.split("=");
            map.put(pair[0], URLDecoder.decode(pair[1],StandardCharsets.UTF_8));
        }
        return map;
    }
    static Map<String,String> parseJson(String json){
        Map<String,String> map = new HashMap<>();
        if(json==null || json.trim().isEmpty())
            return map;
        json=json.replaceAll("[{}\"]","");
        for(String pair:json.split(",")){

            String[] kv=pair.split(":");
            if(kv.length==2)
                map.put(kv[0].trim(),kv[1].trim());
        }
        return map;
    }
}