package xyz.mpdn.resource.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Storage {
    private long id;
    private String bucket;
    private String path;
    private StorageType storageType;

    public enum StorageType {
        PERMANENT, STAGING
    }
}
