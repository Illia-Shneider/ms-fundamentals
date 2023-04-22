package xyz.mpdn.storage.repository;

import org.springframework.data.repository.ListCrudRepository;
import xyz.mpdn.storage.entity.Storage;

public interface StorageRepository extends ListCrudRepository<Storage, Long> {
}
