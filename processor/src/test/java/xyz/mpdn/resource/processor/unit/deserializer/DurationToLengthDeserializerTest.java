package xyz.mpdn.resource.processor.unit.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.mpdn.resource.processor.deserializer.DurationToLengthDeserializer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DurationToLengthDeserializerTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonParser jsonParser;

    @BeforeEach
    void setUp() throws IOException {
        when(jsonParser.getText()).thenReturn("78000");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void deserialize() throws IOException {
        //given
         var durationToLengthDeserializer = new DurationToLengthDeserializer();
        //when
        var result = durationToLengthDeserializer.deserialize(jsonParser, deserializationContext);
        //then
        assertEquals("1:18", result);
    }
}