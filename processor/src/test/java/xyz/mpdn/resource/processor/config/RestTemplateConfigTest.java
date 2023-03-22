package xyz.mpdn.resource.processor.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ContextConfiguration(classes = {RestTemplateConfig.class})
@ActiveProfiles({"test"})
@ExtendWith(SpringExtension.class)
class RestTemplateConfigTest {

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Method under test: {@link RestTemplateConfig#restTemplate(RestTemplateBuilder)}
     */
    @Test
    void testRestTemplate() {
        assertNull(restTemplateConfig.restTemplate(mock(RestTemplateBuilder.class)));
    }

}