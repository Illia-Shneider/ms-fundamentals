package xyz.mpdn.resource.processor.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.dto.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceToSongConverter {
    private final ObjectMapper mapper;

    private final BodyContentHandler bodyContentHandler;
    private final Metadata metadata;
    private final ParseContext parseContext;
    private final Mp3Parser mp3Parser;


    public Song convert(InputStream stream) throws TikaException, IOException, SAXException {

        mp3Parser.parse(stream, bodyContentHandler, metadata, parseContext);

        var metadataMap = Stream.of(metadata.names())
                .collect(Collectors.toMap(Function.identity(), metadata::get));

        log.debug("Resource metadata: {}", metadataMap);

        return mapper.convertValue(
                metadataMap,
                Song.class
        );
    }
}
