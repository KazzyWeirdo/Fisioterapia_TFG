package com.tfg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasAnyScope;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtDecoder jwtDecoder;
    private static final String[] WHITE_LIST_URL = {
            "/physiotherapist/login",
            "/h2-console/**",
            "/webjars/**",
            "/v3/api-docs/**", //this is for swagger
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/training-session/create"};

    public SecurityConfiguration(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable()) //This is to disable the csrf protection. It is not needed for this project since the application is stateless (and we are using JWT)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(WHITE_LIST_URL).permitAll();
                    auth.requestMatchers("/indiba/**").access(hasScope("USER"));
                    auth.requestMatchers("/patient/**").access(hasScope("USER"));
                    auth.requestMatchers("/pni/**").access(hasScope("USER"));
                    auth.requestMatchers("/statistics/**").access(hasScope("USER"));
                    auth.requestMatchers("/training-session/**").access(hasScope("USER"));
                    auth.requestMatchers("/auditlogs/**").access(hasAnyScope("AUDITOR", "ADMIN"));
                    auth.requestMatchers("/physiotherapist/**").access(hasScope("ADMIN"));
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
