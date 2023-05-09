package xyz.mpdn.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Objects;

@Configuration
public class LoggingGlobalFiltersConfigurations {
    final Logger logger =
            LoggerFactory.getLogger(
                    LoggingGlobalFiltersConfigurations.class);

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    var requestData = new HashMap<String, Object>();

                    var type = MDC.get("type");
                    MDC.put("type", "request");

                    requestData.put("responseStatus", Objects.requireNonNull(exchange.getResponse().getStatusCode()).value());
                    requestData.put("requestMethod", exchange.getRequest().getMethod().toString());
                    requestData.put("requestUri", exchange.getRequest().getURI().toString());

                    try {
                        logger.info(objectMapper.writeValueAsString(requestData));
                    } catch (JsonProcessingException e) {
                        logger.error(e.getMessage(), e);
                    } finally {
                        MDC.put("type", type);
                    }

                }));
    }
}