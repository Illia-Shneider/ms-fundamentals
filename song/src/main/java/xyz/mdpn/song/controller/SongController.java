package xyz.mdpn.song.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.mdpn.song.entity.Song;
import xyz.mdpn.song.exception.HTTPNotFoundException;
import xyz.mdpn.song.repository.SongRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/song")
@RequiredArgsConstructor
public class SongController {
    private final SongRepository songRepository;
    @Value("${spring.application.name}")
    private String serviceName;

    @GetMapping
    public Map<String, String> getServiceName() {
        return new HashMap<>() {{
            put("service", serviceName);
        }};
    }

    @GetMapping(value = "/{id}")
    public Song getSong(@PathVariable("id") Optional<Song> song) {
        return song.orElseThrow(HTTPNotFoundException::new);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Song postSong(@Valid @RequestBody Song song) {
        log.debug("Saving song {}", song);
        songRepository.save(song);
        return song;
    }

    @DeleteMapping("/{id}")
    @SuppressWarnings("unused")
    public Map<String, ?> deleteSong(
            //Parse CSV ids to string only for validation purpose
            @Length(max = 199, message = "Valid CSV length of ids should be less then 200 characters") @PathVariable("id") String id,
            @PathVariable("id") List<Long> ids
    ) {

        songRepository.deleteAllById(ids);

        return new HashMap<>() {{
            put("ids", ids);
        }};
    }


}
