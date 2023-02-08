package xyz.mpdn.resource.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.mpdn.resource.entity.Resource;

public interface ResourceRepository extends CrudRepository<Resource, Long> {
}
