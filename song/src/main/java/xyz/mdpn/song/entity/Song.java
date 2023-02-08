package xyz.mdpn.song.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import xyz.mdpn.song.serializer.DateToLengthSerializer;
import xyz.mdpn.song.validator.AfterDate;

import java.util.Date;

@Entity
@Data
public class Song {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Resource Id must be specified.")
    private Long resourceId;
    @NotNull(message = "Name must be specified.")
    private String name;
    @NotNull(message = "Artist must be specified.")
    private String artist;
    @NotNull(message = "Album must be specified.")
    private String album;

    @NotNull(message = "Track length must be specified.")
    @Temporal(TemporalType.TIME)
    @JsonSerialize(using = DateToLengthSerializer.class)
    @JsonFormat(pattern = "mm:ss")
    private Date length;

    @NotNull(message = "Track year must be specified.")
    @PastOrPresent(message = "Track year must be before or current year")
    @AfterDate(value = "1875", format = "yyyy", message = "Track year must be after or equal to 1875")
    @JsonFormat(pattern = "yyyy")
    private Date year;
}
