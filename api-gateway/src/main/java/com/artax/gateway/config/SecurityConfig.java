package com.artax.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


//    private final String[] freeResourceUrls = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
//            "/swagger-resources/**", "/api-docs/**", "/aggregate/**", "/actuator/prometheus","/api/authenticate",
//    "eureka/**"};

    @Value("${keycloak.internal-uri}")
    private String internalIssuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String externalIssuerUri;

    private final String[] freeResourceUrls = {
            "/api/authenticate/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/aggregate/**",
            "/actuator/**",
            "/eureka/**", // Matches /eureka/css, /eureka/js, etc.
            "/eureka/",   // Matches the dashboard home with trailing slash
            "/eureka"     // Matches the dashboard home without trailing slash
    };

//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
//        return httpSecurity
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .cors(Customizer.withDefaults()) // Use the CORS bean you defined
//                .authorizeExchange(exchange -> exchange
//                        // 1. Permitted paths MUST come first
//                        .pathMatchers(freeResourceUrls).permitAll()
//                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
//                        // 2. Everything else requires a token
//                        .anyExchange().authenticated()
//                )
//                // 3. This is what's currently triggering the 401
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//                .build();
//    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchange -> exchange
                        // 1. Explicitly whitelist the dashboard AND its assets
                        .pathMatchers("/eureka/**", "/eureka/", "/eureka").permitAll()
                        .pathMatchers("/api/authenticate/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        // 2. All other requests need Keycloak JWT
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                )
                .build();
    }





    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowedOrigins(List.of(
                "https://crm.artax-group.com",  // Add your production domain
                "http://localhost:3000",         // Keep local for testing
                "http://127.0.0.1:3000"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        // This is the REACTIVE version — this fixes the cast error!
       UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        // Direct path to the certs inside the Docker network
        String jwkSetUri = internalIssuerUri + "/protocol/openid-connect/certs";

        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Validate that the token 'iss' matches the public HTTPS domain
        DelegatingOAuth2TokenValidator<Jwt> jwtValidator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(),
                new JwtIssuerValidator(externalIssuerUri)
        );

        jwtDecoder.setJwtValidator(jwtValidator);
        return jwtDecoder;
    }

}
