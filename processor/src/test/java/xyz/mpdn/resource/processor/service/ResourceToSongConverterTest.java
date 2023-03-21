package xyz.mpdn.resource.processor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.dto.Song;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ResourceToSongConverter.class})
@ExtendWith(SpringExtension.class)
class ResourceToSongConverterTest {
    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceToSongConverter resourceToSongConverter;

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
        when(objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(song);
        assertSame(song, resourceToSongConverter.convert(new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(objectMapper).convertValue((Object) any(), (Class<Object>) any());
    }
}

