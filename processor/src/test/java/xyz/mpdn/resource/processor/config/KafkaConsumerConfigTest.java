package xyz.mpdn.resource.processor.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.LogIfLevelEnabled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
@ContextConfiguration(classes = {KafkaConsumerConfig.class})
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties
class KafkaConsumerConfigTest {
    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    /**
     * Method under test: {@link KafkaConsumerConfig#consumerConfig()}
     */
    @Test
    void testConsumerConfig() {
        assertEquals(3, kafkaConsumerConfig.consumerConfig().size());
    }

    /**
     * Method under test: {@link KafkaConsumerConfig#consumerFactory()}
     */
    @Test
    void testConsumerFactory() {
        assertTrue(kafkaConsumerConfig.consumerFactory() instanceof DefaultKafkaConsumerFactory);
    }

    /**
     * Method under test: {@link KafkaConsumerConfig#kafkaListenerContainerFactory(ConsumerFactory)}
     */
    @Test
    void testKafkaListenerContainerFactory() {
        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig();
        DefaultKafkaConsumerFactory<String, String> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(
                new HashMap<>());
        ConcurrentKafkaListenerContainerFactory<String, String> actualKafkaListenerContainerFactoryResult = kafkaConsumerConfig
                .kafkaListenerContainerFactory(defaultKafkaConsumerFactory);
        assertSame(defaultKafkaConsumerFactory, actualKafkaListenerContainerFactoryResult.getConsumerFactory());
        ContainerProperties containerProperties = actualKafkaListenerContainerFactoryResult.getContainerProperties();
        assertNull(containerProperties.getTopics());
        assertNull(containerProperties.getTopicPattern());
        assertNull(containerProperties.getTopicPartitions());
        assertEquals(10000L, containerProperties.getShutdownTimeout());
        assertEquals(5000L, containerProperties.getPollTimeout());
        assertEquals(3.0f, containerProperties.getNoPollThreshold());
        assertEquals(30, containerProperties.getMonitorInterval());
        assertEquals(5.0d, containerProperties.getIdleBeforeDataMultiplier());
        assertEquals(ContainerProperties.EOSMode.V2, containerProperties.getEosMode());
        assertEquals(3, containerProperties.getCommitRetries());
        assertEquals(LogIfLevelEnabled.Level.DEBUG, containerProperties.getCommitLogLevel());
        assertEquals("", containerProperties.getClientId());
        assertEquals(ContainerProperties.AssignmentCommitOption.LATEST_ONLY_NO_TX,
                containerProperties.getAssignmentCommitOption());
        assertEquals(0, containerProperties.getAdviceChain().length);
        assertEquals(5000L, containerProperties.getAckTime());
        assertEquals(ContainerProperties.AckMode.BATCH, containerProperties.getAckMode());
        assertEquals(1, containerProperties.getAckCount());
    }

    /**
     * Method under test: {@link KafkaConsumerConfig#kafkaListenerContainerFactory(ConsumerFactory)}
     */
    @Test
    void testKafkaListenerContainerFactory2() {
        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig();
        DefaultKafkaConsumerFactory<String, String> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(
                new HashMap<>(), (Supplier<Deserializer<String>>) mock(Supplier.class),
                (Supplier<Deserializer<String>>) mock(Supplier.class));

        ConcurrentKafkaListenerContainerFactory<String, String> actualKafkaListenerContainerFactoryResult = kafkaConsumerConfig
                .kafkaListenerContainerFactory(defaultKafkaConsumerFactory);
        assertSame(defaultKafkaConsumerFactory, actualKafkaListenerContainerFactoryResult.getConsumerFactory());
        ContainerProperties containerProperties = actualKafkaListenerContainerFactoryResult.getContainerProperties();
        assertNull(containerProperties.getTopics());
        assertNull(containerProperties.getTopicPattern());
        assertNull(containerProperties.getTopicPartitions());
        assertEquals(10000L, containerProperties.getShutdownTimeout());
        assertEquals(5000L, containerProperties.getPollTimeout());
        assertEquals(3.0f, containerProperties.getNoPollThreshold());
        assertEquals(30, containerProperties.getMonitorInterval());
        assertEquals(5.0d, containerProperties.getIdleBeforeDataMultiplier());
        assertEquals(ContainerProperties.EOSMode.V2, containerProperties.getEosMode());
        assertEquals(3, containerProperties.getCommitRetries());
        assertEquals(LogIfLevelEnabled.Level.DEBUG, containerProperties.getCommitLogLevel());
        assertEquals("", containerProperties.getClientId());
        assertEquals(ContainerProperties.AssignmentCommitOption.LATEST_ONLY_NO_TX,
                containerProperties.getAssignmentCommitOption());
        assertEquals(0, containerProperties.getAdviceChain().length);
        assertEquals(5000L, containerProperties.getAckTime());
        assertEquals(ContainerProperties.AckMode.BATCH, containerProperties.getAckMode());
        assertEquals(1, containerProperties.getAckCount());
    }
}

