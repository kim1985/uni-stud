package com.uni.unistud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configurazione Spring Security per coesistere con il nostro JwtFilter
 *
 * Strategia:
 * - Permette accesso libero a TUTTI gli endpoint
 * - Disabilita l'autenticazione automatica di Spring Security
 * - Lascia che il nostro JwtFilter gestisca l'autenticazione
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configurazione che bypassa Spring Security mantenendolo attivo
     *
     * Configurazioni:
     * - CSRF disabilitato (non serve per API REST)
     * - Sessioni stateless (non serve stato server-side)
     * - Tutti gli endpoint permettono accesso libero
     * - Il nostro JwtFilter gestisce la sicurezza reale
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disabilita CSRF - non necessario per API REST stateless
                .csrf(csrf -> csrf.disable())

                // Sessioni stateless - non mantiene stato sul server
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // BYPASS COMPLETO - permette tutto
                // Il nostro JwtFilter si occuperà della sicurezza reale
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Permette accesso a tutti gli endpoint
                );

        return http.build();
    }

    // ALTERNATIVA: Configurazione più specifica (se vuoi maggiore controllo)
/*
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Endpoint pubblici espliciti
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()

            // Tutti gli altri endpoint - lascia passare
            // Il nostro JwtFilter deciderà se bloccare o meno
            .anyRequest().permitAll()
        );

    return http.build();
}
*/
}
