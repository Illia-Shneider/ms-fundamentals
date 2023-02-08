package xyz.mdpn.song.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToLengthSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date length, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        var formatter = new SimpleDateFormat("mm:ss");
        jsonGenerator.writeString(formatter.format(length));
    }
}
