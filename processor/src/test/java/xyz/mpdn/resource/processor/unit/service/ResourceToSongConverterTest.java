package xyz.mpdn.resource.processor.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.dto.Song;
import xyz.mpdn.resource.processor.service.ResourceToSongConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "SpellCheckingInspection"})
@ContextConfiguration(classes = {ResourceToSongConverter.class})
@ExtendWith(SpringExtension.class)
class ResourceToSongConverterTest {
    @MockBean
    private BodyContentHandler bodyContentHandler;

    @MockBean
    private Metadata metadata;

    @MockBean
    private Mp3Parser mp3Parser;

    @MockBean
    private ParseContext parseContext;

    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceToSongConverter resourceToSongConverter;

    /**
     * Method under test: {@link ResourceToSongConverter#convert(InputStream)}
     */
    @Test
    void testConvert2() throws IOException, IllegalArgumentException, TikaException, SAXException {
        when(objectMapper.convertValue(any(), (Class<Object>) any())).thenReturn("Convert Value");
        when(metadata.get((String) any())).thenReturn("Get");
        when(metadata.names()).thenReturn(new String[]{"Names"});
        doThrow(new TikaException("Resource metadata: {}")).when(mp3Parser)
                .parse(any(), any(), any(), any());
        assertThrows(TikaException.class,
                () -> resourceToSongConverter.convert(new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8))));
        verify(mp3Parser).parse(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link ResourceToSongConverter#convert(InputStream)}
     */
    @Test
    void testConvert() throws IOException, IllegalArgumentException, TikaException, SAXException {
        Song song = new Song();
        song.setAlbum("Resource metadata: {}");
        song.setArtist("Resource metadata: {}");
        song.setLength("Resource metadata: {}");
        song.setName("Resource metadata: {}");
        song.setResourceId("42");
        song.setYear("Resource metadata: {}");
        when(objectMapper.convertValue(any(), (Class<Object>) any())).thenReturn(song);
        when(metadata.get((String) any())).thenReturn("Get");
        when(metadata.names()).thenReturn(new String[]{"Names"});
        doNothing().when(mp3Parser)
                .parse(any(), any(), any(), any());
        assertSame(song, resourceToSongConverter.convert(new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8))));
        verify(objectMapper).convertValue(any(), (Class<Object>) any());
        verify(metadata).get((String) any());
        verify(metadata).names();
        verify(mp3Parser).parse(any(), any(), any(), any());
    }
}

