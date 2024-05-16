package org.todo_backend.dto.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.todo_backend.dto.Todo;

import java.io.IOException;
import java.time.LocalDateTime;

public class TodoDeserializer extends JsonDeserializer<Todo> {
    @Override
    public Todo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode jsonNode = jsonParser.readValueAsTree();
        long id = jsonNode.get("id").asLong();
        String todo = jsonNode.get("todo").asText();
        String dateText = jsonNode.get("date").asText();


        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateText);
        } catch (Exception _) {
            dateTime = LocalDateTime.now();
        }

        return new Todo(id, todo, dateTime);
    }
}
