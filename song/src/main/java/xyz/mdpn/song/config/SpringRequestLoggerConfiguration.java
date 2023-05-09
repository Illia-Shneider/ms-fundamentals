package xyz.mdpn.song.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class SpringRequestLoggerConfiguration {

    @Bean
    public HttpExchangeRepository httpTraceRepository() {

        return new InMemoryHttpExchangeRepository() {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            Logger logger = LoggerFactory.getLogger(SpringRequestLoggerConfiguration.class);

            @Override
            public void add(HttpExchange trace) {
                var type = MDC.get("type");
                var requestData = new HashMap<String,Object>();

                MDC.put("type", "request");

                requestData.put("responseStatus", trace.getResponse().getStatus());
                requestData.put("requestMethod", trace.getRequest().getMethod());
                requestData.put("requestUri", trace.getRequest().getUri());

                try {
                    logger.info(objectMapper.writeValueAsString(requestData));
                    logger.info(objectMapper.writeValueAsString(trace));
                } catch (JsonProcessingException e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    MDC.put("type", type);
                }

                super.add(trace);
            }
        };
    }
}
