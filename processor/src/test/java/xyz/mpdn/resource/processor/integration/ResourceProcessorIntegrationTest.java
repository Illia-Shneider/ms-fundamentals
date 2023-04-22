package xyz.mpdn.resource.processor.integration;

import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.ProcessorApplication;
import xyz.mpdn.resource.processor.config.KafkaConsumerConfig;
import xyz.mpdn.resource.processor.config.KafkaTopicConfig;
import xyz.mpdn.resource.processor.dto.Song;
import xyz.mpdn.resource.processor.listener.KafkaResourceListener;
import xyz.mpdn.resource.processor.service.ResourceProcessor;
import xyz.mpdn.resource.processor.service.ResourceToSongConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ProcessorApplication.class})
@EnableConfigurationProperties
@ContextConfiguration(classes = {ProcessorApplication.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ResourceProcessorIntegrationTest {

    private final URI uri = new URI("test-resource");
    private final Song song = new Song();

    @Mock
    private ServiceInstance serviceInstance;
    @MockBean
    private ResourceToSongConverter toSongConverter;

    @MockBean
    private KafkaConsumerConfig kafkaConsumerConfig;

    @MockBean
    private KafkaTopicConfig kafkaTopicConfig;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private KafkaResourceListener kafkaResourceListener;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Autowired
    private ResourceProcessor resourceProcessor;

    ResourceProcessorIntegrationTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() throws TikaException, IOException, SAXException {
        when(serviceInstance.getUri()).thenReturn(uri);
        when(discoveryClient.getInstances(any())).thenReturn(List.of(serviceInstance));
        when(restTemplate.execute(any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8)));
        when(restTemplate.postForObject(any(), any(), any())).thenReturn("Success!");
        when(toSongConverter.convert(any())).thenReturn(song);
    }

    /**
     * Method under test: {@link ResourceProcessor#process(String)}
     */
    @Test
    void testProcess() throws IOException, TikaException, SAXException {
        resourceProcessor.process("42");
    }
}

