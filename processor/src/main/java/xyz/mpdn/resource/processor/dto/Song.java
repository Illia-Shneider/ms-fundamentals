package xyz.mpdn.resource.processor.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import xyz.mpdn.resource.processor.deserializer.DurationToLengthDeserializer;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {

    @JsonAlias("title")
    private String name;

    @JsonAlias("xmpDM:artist")
    private String artist;

    @JsonAlias("xmpDM:album")
    private String album;

    @JsonAlias("xmpDM:duration")
    @JsonDeserialize(using = DurationToLengthDeserializer.class)
    private String length;

    private String resourceId;

    @JsonAlias("xmpDM:releaseDate")
    private String year;
}
