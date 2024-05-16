package org.todo_backend.util;

import com.sun.net.httpserver.HttpExchange;
import org.todo_backend.controller.TodoController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtil {
    private static final Logger LOGGER = Logger.getLogger(TodoController.class.getName());

    public static String parseIdFromURI(String uri) {

        String newUrl = uri.substring(0, uri.lastIndexOf("/") + 1);

        // Extract the ID from the URI using regex
        Pattern pattern = Pattern.compile(STR."\{newUrl}(\\d+)");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void sendResponse(HttpExchange exchange, byte[] bytes, int port) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(port, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    public static void setCors(HttpExchange exchange, String origin) {

        try {
            // Set CORS headers to allow requests from any origin
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", origin);
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type,Authorization");

            // Respond to preflight requests immediately and return status 200
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(200, -1);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex.getMessage());
        }
    }
}
