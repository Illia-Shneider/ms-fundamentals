package xyz.mpdn.resource.service;

import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class S3Service {
    private final MinioClient minioClient;

    private Optional<StatObjectResponse> getStat(String bucket, String path, String uuid) {
        StatObjectResponse stat = null;

        try {
            stat = minioClient.statObject(
                    StatObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(composeFileName(path, uuid))
                            .build());
        } catch (Exception e) {
            log.error("Happened error when get object stat from minio: ", e);
        }

        return Optional.ofNullable(stat);
    }

    public Optional<InputStream> get(String bucket, String path, String uuid) {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(composeFileName(path, uuid))
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get object from minio: ", e);
        }

        return Optional.ofNullable(stream);
    }

    public Optional<InputStream> get(String bucket, String path, String uuid, Long start, Long end) {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(composeFileName(path, uuid))
                    .offset(start)
                    .length(end - start + 1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get object by range from minio: ", e);
        }

        return Optional.ofNullable(stream);
    }

    public void delete(String bucket, String path, String uuid) {

        var rObject = RemoveObjectArgs
                .builder()
                .bucket(bucket)
                .object(composeFileName(path, uuid))
                .build();

        try {
            minioClient.removeObject(rObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

//    public List<String> delete(List<String> uuids) {
//        var objects = uuids
//                .stream()
//                .map(DeleteObject::new)
//                .toList();
//
//        var rObjects = RemoveObjectsArgs
//                .builder()
//                .bucket(bucketName)
//                .objects(objects)
//                .build();
//
//        var results = minioClient.removeObjects(rObjects);
//
//        var undeleted = StreamSupport
//                .stream(results.spliterator(), false)
//                .map(result -> {
//                    try {
//                        return result.get();
//                    } catch (Exception exception) {
//                        log.error("Error in deleting object", exception);
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .peek(de -> log.error(String.format("Error in deleting object %s; %s", de.objectName(), de.message())))
//                .map(DeleteError::objectName)
//                .toList();
//
//        return uuids
//                .stream()
//                .filter(Predicate.not(undeleted::contains))
//                .toList();
//    }

    public Optional<ResourceInfo> info(String bucket, String path, String uuid) {
        return getStat(bucket, path, uuid)
                .map(s -> s::size);
    }

    public Optional<String> store(String bucket, String path, InputStream stream, Long size) {
        var uuid = UUID.randomUUID().toString();

        try {
            ensureBucketExists(bucket);
            uploadFile(bucket, stream, composeFileName(path, uuid), size);
        } catch (Exception e) {
            uuid = null;
            log.error("Unable to upload file", e);
        }

        return Optional.ofNullable(uuid);
    }

    public boolean move(String source_bucket, String source_path, String bucket, String path, String uuid) {

        try {
            ensureBucketExists(bucket);

            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(composeFileName(path, uuid))
                            .source(
                                    CopySource.builder()
                                            .bucket(source_bucket)
                                            .object(composeFileName(source_path, uuid))
                                            .build())
                            .build());

            delete(source_bucket, source_path, uuid);
        } catch (Exception e) {

            log.error("Unable to move file", e);
            return false;
        }

        return true;
    }


    private String composeFileName(String path, String uuid) {
        return String.format("%s/%s", path, uuid)
                .replaceAll("^/+", "")
                .replaceAll("/+", "/");
    }


    private void uploadFile(String bucket, InputStream stream, String filename, Long size) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(filename)
                        .stream(stream, size, -1)
                        .build());
    }

    private void ensureBucketExists(String bucket) throws Exception {
        var found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            createBucket(bucket);
        }
    }

    private void createBucket(String bucket) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }
}
