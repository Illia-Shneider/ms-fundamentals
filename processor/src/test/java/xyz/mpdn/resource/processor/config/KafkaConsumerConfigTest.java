package xyz.mpdn.resource.processor.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.function.Supplier;

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

@ContextConfiguration(classes = {KafkaConsumerConfig.class})
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application-test.properties")
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
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@2f9d8c80 testClass = xyz.mpdn.resource.processor.config.DiffblueFakeClass22, locations = [], classes = [xyz.mpdn.resource.processor.ProcessorApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [[ImportsContextCustomizer@55300bfa key = [@org.springframework.boot.context.properties.EnableConfigurationProperties({}), @org.springframework.test.context.BootstrapWith(org.springframework.boot.test.context.SpringBootTestContextBootstrapper.class), @org.springframework.context.annotation.PropertySource(name="", factory=org.springframework.core.io.support.PropertySourceFactory.class, ignoreResourceNotFound=false, encoding="", value=/* Warning type mismatch! "java.lang.String[classpath:application-test.properties]" */), @org.springframework.boot.test.context.SpringBootTest(args={}, webEnvironment=MOCK, useMainMethod=NEVER, value={}, properties={}, classes={}), @org.springframework.context.annotation.Import({org.springframework.boot.context.properties.EnableConfigurationPropertiesRegistrar.class})]], org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@6a5d5000, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@11a1dd54, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@5ad9e905, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@51ed270b, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@9da1, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@1236527b, org.springframework.boot.test.context.SpringBootTestAnnotation@420467cf], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   org.springframework.context.ApplicationContextException: Failed to start bean 'org.springframework.kafka.config.internalKafkaListenerEndpointRegistry'
        //       at java.lang.Iterable.forEach(Iterable.java:75)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   org.apache.kafka.common.KafkaException: Failed to construct kafka consumer
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:830)
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:666)
        //       at java.lang.Iterable.forEach(Iterable.java:75)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   org.apache.kafka.common.config.ConfigException: No resolvable bootstrap urls given in bootstrap.servers
        //       at org.apache.kafka.clients.ClientUtils.parseAndValidateAddresses(ClientUtils.java:89)
        //       at org.apache.kafka.clients.ClientUtils.parseAndValidateAddresses(ClientUtils.java:48)
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:732)
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:666)
        //       at java.lang.Iterable.forEach(Iterable.java:75)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   See https://diff.blue/R026 to resolve this issue.

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
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@2f9d8c80 testClass = xyz.mpdn.resource.processor.config.DiffblueFakeClass22, locations = [], classes = [xyz.mpdn.resource.processor.ProcessorApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [[ImportsContextCustomizer@55300bfa key = [@org.springframework.boot.context.properties.EnableConfigurationProperties({}), @org.springframework.test.context.BootstrapWith(org.springframework.boot.test.context.SpringBootTestContextBootstrapper.class), @org.springframework.context.annotation.PropertySource(name="", factory=org.springframework.core.io.support.PropertySourceFactory.class, ignoreResourceNotFound=false, encoding="", value=/* Warning type mismatch! "java.lang.String[classpath:application-test.properties]" */), @org.springframework.boot.test.context.SpringBootTest(args={}, webEnvironment=MOCK, useMainMethod=NEVER, value={}, properties={}, classes={}), @org.springframework.context.annotation.Import({org.springframework.boot.context.properties.EnableConfigurationPropertiesRegistrar.class})]], org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@6a5d5000, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@11a1dd54, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@5ad9e905, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@51ed270b, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@9da1, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@1236527b, org.springframework.boot.test.context.SpringBootTestAnnotation@420467cf], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   org.springframework.context.ApplicationContextException: Failed to start bean 'org.springframework.kafka.config.internalKafkaListenerEndpointRegistry'
        //       at java.lang.Iterable.forEach(Iterable.java:75)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   org.apache.kafka.common.KafkaException: Failed to construct kafka consumer
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:830)
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:666)
        //       at java.lang.Iterable.forEach(Iterable.java:75)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   org.apache.kafka.common.config.ConfigException: No resolvable bootstrap urls given in bootstrap.servers
        //       at org.apache.kafka.clients.ClientUtils.parseAndValidateAddresses(ClientUtils.java:89)
        //       at org.apache.kafka.clients.ClientUtils.parseAndValidateAddresses(ClientUtils.java:48)
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:732)
        //       at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:666)
        //       at java.lang.Iterable.forEach(Iterable.java:75)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:1002)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
        //   See https://diff.blue/R026 to resolve this issue.

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

