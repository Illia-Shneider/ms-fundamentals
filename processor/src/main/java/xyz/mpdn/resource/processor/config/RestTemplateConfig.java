package xyz.mpdn.resource.processor.config;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer restTemplateBuilderConfigurer) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return restTemplateBuilderConfigurer.configure(builder);
    }
}
