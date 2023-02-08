package xyz.mpdn.resource.service;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private Optional<StatObjectResponse> getStat(String uuid) {
        StatObjectResponse stat = null;

        try {
            stat = minioClient.statObject(
                    StatObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(uuid)
                            .build());
        } catch (Exception e) {
            log.error("Happened error when get object stat from minio: ", e);
        }

        return Optional.ofNullable(stat);
    }

    public Optional<InputStream> get(String uuid) {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uuid)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get object from minio: ", e);
        }

        return Optional.ofNullable(stream);
    }

    public Optional<InputStream> get(String uuid, Long start, Long end) {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uuid)
                    .offset(start)
                    .length(end - start + 1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get object by range from minio: ", e);
        }

        return Optional.ofNullable(stream);
    }

    public void delete(String uuid) {

        var rObject = RemoveObjectArgs
                .builder()
                .bucket(bucketName)
                .object(uuid)
                .build();

        try {
            minioClient.removeObject(rObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> delete(List<String> uuids) {
        var objects = uuids
                .stream()
                .map(DeleteObject::new)
                .toList();

        var rObjects = RemoveObjectsArgs
                .builder()
                .bucket(bucketName)
                .objects(objects)
                .build();

        var results = minioClient.removeObjects(rObjects);

        var undeleted = StreamSupport
                .stream(results.spliterator(), false)
                .map(result -> {
                    try {
                        return result.get();
                    } catch (Exception exception) {
                        log.error("Error in deleting object", exception);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .peek(de -> log.error(String.format("Error in deleting object %s; %s", de.objectName(), de.message())))
                .map(DeleteError::objectName)
                .toList();

        return uuids
                .stream()
                .filter(Predicate.not(undeleted::contains))
                .toList();
    }

    public Optional<ResourceInfo> info(String uuid) {
        return getStat(uuid)
                .map(s -> s::size);
    }

    public Optional<String> store(InputStream stream, Long size) {
        var uuid = UUID.randomUUID().toString();

        try {
            ensureBucketExists();
            uploadFile(stream, uuid, size);
        } catch (Exception e) {
            uuid = null;
            log.error("Unable to upload file", e);
        }

        return Optional.ofNullable(uuid);
    }

    private void uploadFile(InputStream stream, String filename, Long size) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .stream(stream, size, -1)
                        .build());
    }

    private void ensureBucketExists() throws Exception {
        var found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            createBucket();
        }
    }

    private void createBucket() throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
}
