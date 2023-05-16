package xyz.mpdn.resource.config;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        var restTemplate = restTemplateBuilder
                .build();

        restTemplate.getInterceptors().add((request, body, execution) -> {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                return execution.execute(request, body);
            }

            if (!(authentication.getCredentials() instanceof AbstractOAuth2Token token)) {
                return execution.execute(request, body);
            }

            request.getHeaders().setBearerAuth(token.getTokenValue());
            return execution.execute(request, body);
        });

        return restTemplate;
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer restTemplateBuilderConfigurer) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return restTemplateBuilderConfigurer.configure(builder);
    }
}
