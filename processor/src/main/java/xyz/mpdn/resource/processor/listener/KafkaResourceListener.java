package xyz.mpdn.resource.processor.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import xyz.mpdn.resource.processor.service.ResourceProcessor;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaResourceListener {

    private final ResourceProcessor resourceProcessor;

    @KafkaListener(topics = "resource", groupId = "ms_fundamentals")
    void listener(String id) {
        log.info("Resource processing is triggered for : {}", id);

        try {
            resourceProcessor.process(id);
        }catch (Exception e){
            log.error("Error happened during resource processing", e);
        }

        log.info("Resource \"{}\" successfully processed", id);
    }
}
