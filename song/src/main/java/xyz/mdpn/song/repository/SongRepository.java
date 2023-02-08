package xyz.mdpn.song.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.mdpn.song.entity.Song;

public interface SongRepository extends CrudRepository<Song, Long> {
}
