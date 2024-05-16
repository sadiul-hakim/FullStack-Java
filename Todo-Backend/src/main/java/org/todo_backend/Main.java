package org.todo_backend;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.todo_backend.controller.HomeController;
import org.todo_backend.controller.TodoController;
import org.todo_backend.dto.Todo;
import org.todo_backend.repository.TodoRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {

        // Create the server
        var server = HttpServer.create(new InetSocketAddress(9095), 0);
        System.out.println("Server is listening to post : 9095");

        HomeController homeController = new HomeController();
        HttpContext homeContext = server.createContext("/");
        homeContext.setHandler(homeController::home);

        TodoController controller = new TodoController();
        HttpContext saveContext = server.createContext("/todo/save");
        saveContext.setHandler(controller::saveTodo);
        HttpContext getByIdContext = server.createContext("/todo/get-by-id");
        getByIdContext.setHandler(controller::getById);
        HttpContext getAllContext = server.createContext("/todo/get-all");
        getAllContext.setHandler(controller::getAllTodo);
        HttpContext deleteContext = server.createContext("/todo/delete");
        deleteContext.setHandler(controller::delete);

        server.start();
    }
}