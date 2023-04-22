package xyz.mpdn.storage.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String bucket;
    private String path;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    public enum StorageType {
        PERMANENT, STAGING
    }
}
