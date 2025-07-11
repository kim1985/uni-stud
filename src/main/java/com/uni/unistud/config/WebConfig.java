// 7. Configurazione semplice
// WebConfig.java
package com.uni.unistud.config;

import com.uni.unistud.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione web per registrare filtri personalizzati
 *
 * Responsabilità:
 * - Registra il JwtFilter nella catena di filtri di Spring Boot
 * - Configura su quali URL applicare il filtro JWT
 * - Imposta l'ordine di esecuzione dei filtri
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig {

    /**
     * Il nostro filtro JWT personalizzato
     * Iniettato automaticamente da Spring grazie a @RequiredArgsConstructor
     */
    private final JwtFilter jwtFilter;

    /**
     * Registra il JwtFilter nella catena di filtri di Spring Boot
     * <p>
     * Configurazione:
     * - URL Pattern: /api/* (solo endpoint API, non risorse statiche)
     * - Ordine: 1 (eseguito per primo, prima di altri filtri)
     * - Tipo: FilterRegistrationBean (gestione Spring Boot)
     *
     * @return bean di registrazione del filtro configurato
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        // Crea il bean di registrazione per il nostro filtro
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();

        // Associa il nostro JwtFilter al bean di registrazione
        registration.setFilter(jwtFilter);

        // PATTERN URL: applica il filtro solo agli endpoint /api/*
        // Vantaggi:
        // - Non interferisce con risorse statiche (/css, /js, /images)
        // - Non blocca la console H2 (/h2-console)
        // - Non rallenta pagine HTML non protette
        registration.addUrlPatterns("/api/*");

        // ORDINE ESECUZIONE: 1 = alta priorità
        // Il JwtFilter viene eseguito PRIMA di altri filtri
        // Importante per bloccare richieste non autorizzate il prima possibile
        registration.setOrder(100);

        return registration;
    }
    /*
     * NOTA: Alternativa senza FilterRegistrationBean
     *
     * Si potrebbe usare @Component sul JwtFilter stesso, ma FilterRegistrationBean
     * offre maggiore controllo su:
     * - Pattern URL specifici
     * - Ordine di esecuzione
     * - Configurazioni avanzate (init parameters, etc.)
     */
}

// 9. Esempio di utilizzo
/*
1. REGISTRAZIONE:
POST /api/auth/register
{
    "firstName": "Mario",
    "lastName": "Rossi",
    "email": "mario@test.it",
    "password": "123456"
}

2. LOGIN:
POST /api/auth/login
{
    "email": "mario@test.it",
    "password": "123456"
}
Risposta: {"token": "eyJ...", "email": "mario@test.it", "fullName": "Mario Rossi"}

3. USO TOKEN:
GET /api/students
Header: Authorization: Bearer eyJ...

FATTO! Sistema JWT funzionante in 9 file semplici.
*/