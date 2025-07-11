// 6. Filtro JWT semplice
// JwtFilter.java
package com.uni.unistud.filter;

import com.uni.unistud.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtro JWT che protegge gli endpoint dell'API
 *
 * Funzionamento:
 * 1. Intercetta TUTTE le richieste a /api/*
 * 2. Permette accesso libero a /api/auth/* (login/register)
 * 3. Per altri endpoint, verifica presenza e validità del token JWT
 * 4. Se token valido: estrae email e passa la richiesta al controller
 * 5. Se token mancante/invalido: blocca con errore 401
 */
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    /**
     * Metodo principale del filtro - eseguito per ogni richiesta HTTP
     *
     * @param request richiesta HTTP in arrivo
     * @param response risposta HTTP da inviare
     * @param chain catena di filtri (per continuare l'elaborazione)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast a HttpServlet per accedere a metodi HTTP specifici
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // DEBUG TEMPORANEO
        System.out.println("=== DEBUG JWT FILTER ===");
        System.out.println("Path richiesta: " + path);
        System.out.println("Method: " + httpRequest.getMethod());

        // ENDPOINT PUBBLICI - non richiedono autenticazione
        if (path.startsWith("/api/auth/") || path.startsWith("/h2-console")) {
            System.out.println("ENDPOINT PUBBLICO - passo oltre");
            chain.doFilter(request, response);  // Passa al prossimo filtro/controller
            return;
        }

        // CONTROLLO HEADER AUTHORIZATION
        // Formato atteso: "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("ERRORE: Header Authorization mancante o malformato");
            sendUnauthorized(httpResponse, "Token mancante");
            return;  // Blocca la richiesta
        }

        // ESTRAZIONE TOKEN
        // Rimuove "Bearer " (7 caratteri) per ottenere solo il token
        String token = authHeader.substring(7);
        System.out.println("Token estratto: " + token.substring(0, Math.min(20, token.length())) + "...");

        // VALIDAZIONE TOKEN
        // JwtUtil verifica firma, formato e scadenza
        boolean isValid = jwtUtil.validateToken(token);
        System.out.println("Token valido: " + isValid);

        if (!isValid) {
            System.out.println("ERRORE: Token non valido");
            sendUnauthorized(httpResponse, "Token non valido o scaduto");
            return;  // Blocca la richiesta
        }

        // TOKEN VALIDO - estrae email e continua
        String email = jwtUtil.getEmailFromToken(token);
        System.out.println("Email estratta: " + email);

        // Salva l'email nella richiesta per uso nei controller
        // I controller possono recuperarla con: request.getAttribute("userEmail")
        httpRequest.setAttribute("userEmail", email);
        System.out.println("SUCCESSO: passo al controller");

        // Passa la richiesta al prossimo elemento della catena (controller)
        chain.doFilter(request, response);
    }

    /**
     * Invia risposta di errore 401 Unauthorized in formato JSON
     *
     * Chiamato quando:
     * - Header Authorization mancante
     * - Token JWT non valido o scaduto
     *
     * @param response risposta HTTP
     * @param message messaggio di errore da includere
     */
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // HTTP 401
        response.setContentType("application/json");             // Risposta JSON
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}