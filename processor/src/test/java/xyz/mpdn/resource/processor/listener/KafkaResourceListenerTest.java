package xyz.mpdn.resource.processor.listener;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xml.sax.SAXException;
import xyz.mpdn.resource.processor.service.ResourceProcessor;

@ContextConfiguration(classes = {KafkaResourceListener.class})
@ExtendWith(SpringExtension.class)
class KafkaResourceListenerTest {
    @Autowired
    private KafkaResourceListener kafkaResourceListener;

    @MockBean
    private ResourceProcessor resourceProcessor;

    /**
     * Method under test: {@link KafkaResourceListener#listener(String)}
     */
    @Test
    void testListener() throws IOException, TikaException, SAXException {
        doNothing().when(resourceProcessor).process((String) any());
        kafkaResourceListener.listener("42");
        verify(resourceProcessor).process((String) any());
    }
}

