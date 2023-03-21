package xyz.mpdn.resource.processor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = ProcessorApplication.class,
        // Normally spring.cloud.config.enabled:true is the default but since we have the
        // config server on the classpath we need to set it explicitly
        webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class ProcessorApplicationTests {

    @Test
    void contextLoads() {
    }

}
