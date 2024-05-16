package org.todo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.todo_backend.util.ServerUtil;

import java.io.IOException;
import java.util.Map;

public class HomeController {
    private final ObjectMapper mapper = new ObjectMapper();

    public void home(HttpExchange exchange) {
        Map<String, String> welcome = Map.of("message", "Welcome Here");
        try {
            ServerUtil.sendResponse(exchange, mapper.writeValueAsString(welcome).getBytes(), 200);
        } catch (IOException e) {
            try {
                ServerUtil.sendResponse(exchange, e.getMessage().getBytes(), 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
