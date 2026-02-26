package org.messaging.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                     CorsConfigurationSource corsConfigurationSource) {

        CookieServerCsrfTokenRepository tokenRepository =
                CookieServerCsrfTokenRepository.withHttpOnlyFalse();

        XorServerCsrfTokenRequestAttributeHandler delegate =
                new XorServerCsrfTokenRequestAttributeHandler();

        ServerCsrfTokenRequestHandler requestHandler = delegate::handle;


        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                )
                .build();
    }

    @Bean
    WebFilter csrfCookieInitializerFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfTokenMono = exchange.getAttribute(CsrfToken.class.getName());
            return (csrfTokenMono != null ? csrfTokenMono.doOnNext(t -> {}) : Mono.empty())
                    .then(chain.filter(exchange));
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:4200"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}