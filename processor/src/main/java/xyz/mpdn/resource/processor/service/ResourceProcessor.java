package xyz.mpdn.resource.processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.dto.Song;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableRetry
public class ResourceProcessor {
    private final ResourceToSongConverter toSongConverter;
    private final RestTemplate template;
    private final DiscoveryClient discoveryClient;

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 2000))
    private URI getServiceURI(String serviceName) {
        return discoveryClient
                .getInstances(serviceName)
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException(String.format("\"%s\" service is unavailable", serviceName)))
                .getUri();
    }

    private InputStream resourceHttpResponseHandler(ClientHttpResponse clientHttpResponse) throws IOException {
        var body = clientHttpResponse.getBody().readAllBytes();
        log.debug("Resource size: {} bytes", body.length);

        if (body.length < 1) {
            throw new RuntimeException("Resource service returned empty result");
        }

        return new ByteArrayInputStream(body);
    }

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 2000))
    private String postSong(Song song) {
        var songServiceUrl = getServiceURI("song-service");
        log.debug("Song service url: {}", songServiceUrl);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var request = new HttpEntity<>(song, headers);

        return template.postForObject(
                String.format("%s/api/v1/song", songServiceUrl),
                request,
                String.class
        );
    }

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 2000))
    private InputStream getResourceInputStream(String resourceId) {
        var resourceServiceUrl = getServiceURI("resource-service");
        log.debug("Resource service url: {}", resourceServiceUrl);

        return template.execute(
                String.format("%s/api/v1/resource/%s", resourceServiceUrl, resourceId),
                HttpMethod.GET,
                null,
                this::resourceHttpResponseHandler
        );
    }

    public void process(String resourceId) throws TikaException, IOException, SAXException {

        var inputStream = getResourceInputStream(resourceId);

        var song = toSongConverter.convert(inputStream);

        song.setResourceId(resourceId);
        log.debug("Song object: {}", song);

        var response = postSong(song);
        log.debug("Song service response: {}", response);

        var resourceServiceUrl = getServiceURI("resource-service");
        log.debug("Resource service url: {}", resourceServiceUrl);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var request = new HttpEntity<>(song, headers);
        template.exchange(String.format("%s/api/v1/resource/%s", resourceServiceUrl, resourceId), HttpMethod.PUT, request, Void.class);

    }
}
