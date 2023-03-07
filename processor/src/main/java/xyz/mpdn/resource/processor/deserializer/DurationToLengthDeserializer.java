package xyz.mpdn.resource.processor.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DurationToLengthDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var duration = Double.valueOf(jsonParser.getText()).intValue();
        var minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        var seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        return String.format("%s:%s", minutes, seconds);
    }
}
