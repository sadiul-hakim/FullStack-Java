package org.todo_backend.repository;

import org.todo_backend.db.DatabaseConnection;
import org.todo_backend.dto.Todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoRepository {
    private final Logger LOGGER = Logger.getLogger(TodoRepository.class.getName());
    public boolean save(Todo todo) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO todo (id,todo,date) values (0,?,?)")) {
                statement.setString(1, todo.todo());
                statement.setString(2, String.valueOf(Timestamp.valueOf(todo.date())));

                statement.executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO,e.getMessage());
            return false;
        }

        return true;
    }

    public Optional<Todo> getById(long id) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement("select * from todo where id = ?")) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    var todoId = resultSet.getLong("id");
                    var todo = resultSet.getString("todo");
                    var dateStr = resultSet.getString("date");
                    return Optional.of(new Todo(todoId, todo, Timestamp.valueOf(dateStr).toLocalDateTime()));
                }

                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO,e.getMessage());
            return Optional.empty();
        }
    }

    public List<Todo> getAll() {
        List<Todo> list = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement("select * from todo")) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    var todoId = resultSet.getLong("id");
                    var todo = resultSet.getString("todo");
                    var dateStr = resultSet.getString("date");
                    list.add(new Todo(todoId, todo, Timestamp.valueOf(dateStr).toLocalDateTime()));
                }

                return list;
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO,e.getMessage());
            return list;
        }
    }

    public boolean delete(long id) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement("delete from todo where id = ?")) {
                statement.setLong(1, id);
                statement.executeUpdate();

                return true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO,e.getMessage());
            return false;
        }
    }
}
