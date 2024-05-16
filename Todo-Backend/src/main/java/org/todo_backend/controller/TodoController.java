package org.todo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.net.httpserver.HttpExchange;
import org.todo_backend.dto.Todo;
import org.todo_backend.dto.serializer.TodoDeserializer;
import org.todo_backend.dto.serializer.TodoSerializer;
import org.todo_backend.repository.TodoRepository;
import org.todo_backend.util.ServerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoController {
    private final Logger LOGGER = Logger.getLogger(TodoController.class.getName());
    private final TodoRepository repository = new TodoRepository();
    private static final ObjectMapper mapper;
    private final Map<String, String> NOT_FOUND = Map.of("message", "404 Not Found");

    static {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Todo.class, new TodoSerializer());
        module.addDeserializer(Todo.class, new TodoDeserializer());

        mapper.registerModule(module);
    }

    public void saveTodo(HttpExchange exchange) {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {

            ServerUtil.setCors(exchange,"*");

            if (!exchange.getRequestMethod().equals("POST")) {
                String methodMessage = mapper.writeValueAsString(Collections.singletonMap("message", "Method not allowed."));
                ServerUtil.sendResponse(exchange, methodMessage.getBytes(StandardCharsets.UTF_8), 405);
            }

            StringBuilder builder = new StringBuilder();
            bufferedReader.lines().forEach(builder::append);

            String bodyText = builder.toString();
            Todo todo = mapper.readValue(bodyText, Todo.class);
            boolean saved = repository.save(todo);

            String successMessage = mapper.writeValueAsString(Collections.singletonMap("message", "Saved todo successfully."));
            String failureMessage = mapper.writeValueAsString(Collections.singletonMap("message", "Could not save todo."));

            if (saved) {
                ServerUtil.sendResponse(exchange, successMessage.getBytes(StandardCharsets.UTF_8), 200);
            } else {
                ServerUtil.sendResponse(exchange, failureMessage.getBytes(StandardCharsets.UTF_8), 500);
            }

        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            try {
                ServerUtil.sendResponse(exchange, e.getMessage().getBytes(StandardCharsets.UTF_8), 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void getAllTodo(HttpExchange exchange) {
        try {
            ServerUtil.setCors(exchange,"*");

            List<Todo> todo = repository.getAll();
            String todoStr = mapper.writeValueAsString(todo);
            ServerUtil.sendResponse(exchange, todoStr.getBytes(StandardCharsets.UTF_8), 200);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            try {
                ServerUtil.sendResponse(exchange, e.getMessage().getBytes(StandardCharsets.UTF_8), 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void getById(HttpExchange exchange) {
        try {

            ServerUtil.setCors(exchange,"*");

            byte[] notFound = mapper.writeValueAsString(NOT_FOUND).getBytes();
            String requestURI = exchange.getRequestURI().toString();
            String idText = ServerUtil.parseIdFromURI(requestURI);

            if (idText == null || idText.isEmpty()) {
                ServerUtil.sendResponse(exchange, notFound, 404);
            }

            Optional<Todo> todo = repository.getById(Integer.parseInt(idText));
            if (todo.isEmpty()) {
                ServerUtil.sendResponse(exchange, notFound, 404);
            }

            Todo todoObj = todo.get();

            String todoStr = mapper.writeValueAsString(todoObj);
            ServerUtil.sendResponse(exchange, todoStr.getBytes(StandardCharsets.UTF_8), 200);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            try {
                ServerUtil.sendResponse(exchange, e.getMessage().getBytes(StandardCharsets.UTF_8), 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void delete(HttpExchange exchange) {
        try {

            ServerUtil.setCors(exchange,"*");

            byte[] notFound = mapper.writeValueAsString(NOT_FOUND).getBytes();
            String requestURI = exchange.getRequestURI().toString();
            String idText = ServerUtil.parseIdFromURI(requestURI);

            if (idText == null || idText.isEmpty()) {
                ServerUtil.sendResponse(exchange, notFound, 404);
            }

            Optional<Todo> todo = repository.getById(Integer.parseInt(idText));
            if (todo.isEmpty()) {
                ServerUtil.sendResponse(exchange, notFound, 404);
            }

            boolean deleted = repository.delete(Integer.parseInt(idText));

            String successMessage = mapper.writeValueAsString(Collections.singletonMap("message", "Deleted todo successfully."));
            String failureMessage = mapper.writeValueAsString(Collections.singletonMap("message", "Could not delete todo."));

            if (deleted) {
                ServerUtil.sendResponse(exchange, successMessage.getBytes(StandardCharsets.UTF_8), 200);
            } else {
                ServerUtil.sendResponse(exchange, failureMessage.getBytes(StandardCharsets.UTF_8), 500);
            }

        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            try {
                ServerUtil.sendResponse(exchange, e.getMessage().getBytes(StandardCharsets.UTF_8), 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
