package xyz.mpdn.resource.service;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import xyz.mpdn.resource.model.Storage;
import xyz.mpdn.resource.repository.ResourceRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles({"test", "excluded-autoconfig"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Slf4j
class CircuitBreakerTest {

    @Value("${wiremock.server.port}")
    String wiremock_port;
    @Autowired
    StorageService storageService;
    @MockBean
    private ResourceRepository resourceRepository;
    @MockBean
    private MinioClient minioClient;
    @Mock
    private ServiceInstance serviceInstance;
    @MockBean
    private DiscoveryClient discoveryClient;

    @BeforeEach
    void setup() throws URISyntaxException {
        when(serviceInstance.getUri()).thenReturn(new URI("http://localhost:" + wiremock_port));
        when(discoveryClient.getInstances(any())).thenReturn(List.of(serviceInstance));
    }


    @Test
    void getStorageFail() {
        stubFor(
                get(urlMatching("/api/v1/storage"))
                        .willReturn(serverError())
        );

        var storage = storageService.getStorage(Storage.StorageType.PERMANENT);

        assertEquals(999999999, storage.getId());
        assertEquals("resources", storage.getBucket());
    }


    @Test
    void getStorageSuccess() throws URISyntaxException {
        stubFor(
                get(urlMatching("/api/v1/storage"))
                        .willReturn(
                                ok()
                                        .withBody("[{\"id\":1,\"bucket\":\"permanent\",\"path\":\"/permanent\",\"storageType\":\"PERMANENT\"}, {\"id\":2,\"bucket\":\"staging\",\"path\":\"/staging\",\"storageType\":\"STAGING\"}]")
                                        .withHeader("Content-Type", "application/json")
                        )
        );

        var storage = storageService.getStorage(Storage.StorageType.PERMANENT);

        assertEquals(1, storage.getId());
        assertEquals(Storage.StorageType.PERMANENT, storage.getStorageType());

        storage = storageService.getStorage(Storage.StorageType.STAGING);

        assertEquals(2, storage.getId());
        assertEquals(Storage.StorageType.STAGING, storage.getStorageType());

    }

}