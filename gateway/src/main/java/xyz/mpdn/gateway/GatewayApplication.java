package xyz.mpdn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class GatewayApplication {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
				.csrf()
				.disable()
				.authorizeExchange(exchange ->
						exchange.pathMatchers("/api/**").authenticated() //matchers(EndpointRequest.toAnyEndpoint())
								.anyExchange().permitAll()
				)
				.oauth2Login(Customizer.withDefaults())
				.build();
	}

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
//    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
//        http
//
//                .authorizeExchange((exchanges) -> exchanges
//                        .anyExchange().permitAll()
//                );
//
//        return http.build();
//    }

}
