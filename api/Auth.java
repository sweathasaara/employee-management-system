package api;

import com.sun.net.httpserver.HttpExchange;
import java.util.Base64;

public class Auth {

    public static boolean check(HttpExchange e, String requiredRole) {

        try {

            String auth = e.getRequestHeaders().getFirst("Authorization");

            if (auth == null || !auth.startsWith("Basic ")) {
                send401(e);
                return false;
            }

            String base64 = auth.substring(6);
            String decoded = new String(Base64.getDecoder().decode(base64));

            String[] parts = decoded.split(":");
            String user = parts[0];
            String pass = parts[1];

            String role = getRole(user, pass);

            if (role == null) {
                send401(e);
                return false;
            }

            if (!hasPermission(role, requiredRole)) {
                send403(e);
                return false;
            }

            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    private static String getRole(String user, String pass) {

        if (user.equals("admin") && pass.equals("admin123"))
            return "admin";

        if (user.equals("manager") && pass.equals("man123"))
            return "manager";

        if (user.equals("employee") && pass.equals("emp123"))
            return "employee";

        return null;
    }

    private static boolean hasPermission(String role, String required) {

        if (role.equals("admin")) return true;

        if (role.equals("manager") &&
           (required.equals("manager") || required.equals("employee")))
            return true;

        if (role.equals("employee") && required.equals("employee"))
            return true;

        return false;
    }

    private static void send401(HttpExchange e) throws Exception {
        String msg = "{\"error\":\"Unauthorized\"}";
        e.getResponseHeaders().add("WWW-Authenticate", "Basic");
        e.sendResponseHeaders(401, msg.length());
        e.getResponseBody().write(msg.getBytes());
        e.close();
    }

    private static void send403(HttpExchange e) throws Exception {
        String msg = "{\"error\":\"Forbidden\"}";
        e.sendResponseHeaders(403, msg.length());
        e.getResponseBody().write(msg.getBytes());
        e.close();
    }
}