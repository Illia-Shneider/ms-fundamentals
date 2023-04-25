package xyz.mpdn.resource.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.mpdn.resource.exception.HTTPNotFoundException;
import xyz.mpdn.resource.model.Storage;
import xyz.mpdn.resource.model.Storage.StorageType;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StorageService {
    private final DiscoveryClient discoveryClient;
    private final RestTemplate template;

    private URI getServiceURI() {
        return discoveryClient
                .getInstances("storage-service")
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("\"storage-service\" service is unavailable"))
                .getUri();
    }

    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallback")
    public Storage getStorage(StorageType storageType) {

        var storageServiceUrl = getServiceURI();
        log.debug("Resource service url: {}", storageServiceUrl);

        var storages = template.exchange(
                String.format("%s/api/v1/storage", storageServiceUrl),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Storage>>() {
                }
        ).getBody();

        assert storages != null;

        return storages.stream()
                .filter(storage -> storage.getStorageType().equals(storageType))
                .findFirst()
                .orElseThrow(HTTPNotFoundException::new);
    }

    private Storage fallback(StorageType storageType, Exception ex){
        return new Storage()
                .setId(999999999)
                .setStorageType(storageType)
                .setBucket("resources")
                .setPath("/");
    }
}
