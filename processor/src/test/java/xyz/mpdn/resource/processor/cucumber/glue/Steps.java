package xyz.mpdn.resource.processor.cucumber.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.ProcessorApplication;
import xyz.mpdn.resource.processor.config.KafkaConsumerConfig;
import xyz.mpdn.resource.processor.config.KafkaTopicConfig;
import xyz.mpdn.resource.processor.dto.Song;
import xyz.mpdn.resource.processor.listener.KafkaResourceListener;
import xyz.mpdn.resource.processor.service.ResourceProcessor;
import xyz.mpdn.resource.processor.service.ResourceToSongConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProcessorApplication.class})
@CucumberContextConfiguration
@AutoConfigureWireMock(port = 0)
public class Steps {
    private final Song song = getSong();
    @Value("${wiremock.server.port}")
    String wiremock_port;
    private boolean success;
    @Mock
    private ServiceInstance serviceInstance;
    @MockBean
    private ResourceToSongConverter toSongConverter;
    @MockBean
    private KafkaConsumerConfig kafkaConsumerConfig;
    @MockBean
    private KafkaTopicConfig kafkaTopicConfig;
    @MockBean
    private KafkaResourceListener kafkaResourceListener;
    @MockBean
    private DiscoveryClient discoveryClient;
    @Autowired
    private ResourceProcessor resourceProcessor;

    public Steps() throws URISyntaxException {
    }

    static Song getSong() {
        Song song = new Song();
        song.setAlbum("Album");
        song.setName("Song about nothing");
        song.setArtist("Noname");
        song.setLength("1:18");
        song.setYear("2023");
        return song;
    }

    @When("new resource available {string}")
    public void whenNewResource(String id) throws TikaException, IOException, SAXException, URISyntaxException {
        wireMockStubs();

        when(serviceInstance.getUri()).thenReturn(new URI("http://localhost:" + wiremock_port));
        when(discoveryClient.getInstances(any())).thenReturn(List.of(serviceInstance));
        when(toSongConverter.convert(any())).thenReturn(song);

        try {
            resourceProcessor.process(id);
            success = true;
        } catch (Exception e) {
            success = false;

        }
    }

    @Then("success result")
    public void success() {
        Assertions.assertTrue(success);
    }

    @Then("fail")
    public void fail() {
        Assertions.assertFalse(success);
    }


    public void wireMockStubs() {
        stubFor(
                get(urlMatching("/api/v1/resource/(\\d+)"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/octet-stream")
                                .withBody("Hello world!")));

        stubFor(
                put(urlMatching("/api/v1/resource/(\\d+)"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/octet-stream")
                                .withBody("")));

        stubFor(
                post("/api/v1/song")
                        .withRequestBody(matchingJsonPath("$[?(@.resourceId == 41)]"))
                        .willReturn(ok())
        );

        stubFor(
                post("/api/v1/song")
                        .withRequestBody(matchingJsonPath("$[?(@.resourceId != 41)]"))
                        .willReturn(badRequest())
        );
    }
}
