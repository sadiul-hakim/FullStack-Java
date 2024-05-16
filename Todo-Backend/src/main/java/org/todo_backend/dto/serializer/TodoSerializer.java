package org.todo_backend.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.todo_backend.dto.Todo;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class TodoSerializer extends JsonSerializer<Todo> {
    @Override
    public void serialize(Todo todo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();


        jsonGenerator.writeNumberField("id", todo.id());
        jsonGenerator.writeStringField("todo", todo.todo());
        jsonGenerator.writeStringField("date", todo.date().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        jsonGenerator.writeEndObject();
    }
}
