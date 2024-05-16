package org.todo_backend.dto;

import java.time.LocalDateTime;

public record Todo(long id, String todo, LocalDateTime date) {
}
