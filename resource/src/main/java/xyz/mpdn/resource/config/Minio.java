package xyz.mpdn.resource.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class Minio {
    private String url;
    private Bucket bucket;
    private Access access;

    @Bean
    @Primary
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .credentials(access.getKey(), access.getSecret())
                .endpoint(url)
                .build();
    }

    @Data
    public static class Access {
        private String key;
        private String secret;
    }

    @Data
    public static class Bucket {
        private String name;
    }
}
