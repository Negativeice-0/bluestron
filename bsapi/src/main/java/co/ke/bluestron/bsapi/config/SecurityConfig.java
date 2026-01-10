package co.ke.bluestron.bsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Minimal security for Module 1:
 * - Disable CSRF for simplicity (we'll add proper auth in Module 2).
 * - Permit public API endpoints for verification with curl.
 * i/e bash "http://localhost:8080/api/categories or courses or etc".
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/actuator/**",
                    "/api/status",
                    "/api/categories/**",
                    "/api/courses/**",
                    "/api/trainers/**",
                    "/api/testimonials/**",
                    "/api/service-offerings/**",
                    "/api/service-enquiries/**",
                    "/api/registrations/**",
                    "/api/featured-content/**",
                    "/api/blog-posts/**",
                    "/api/homepage/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**"
                ).permitAll()
                .anyRequest().permitAll() // allow everything for now
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
