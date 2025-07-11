// AuthResponse.java
package com.uni.unistud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la risposta di autenticazione
 *
 * Utilizzato per:
 * - Risposta di login (/api/auth/login)
 * - Risposta di registrazione (/api/auth/register)
 * - Verifica stato utente (/api/auth/me)
 *
 * Contiene il token JWT e le informazioni base dell'utente autenticato
 */
@Data                // Lombok: genera getter, setter, toString, equals, hashCode
@AllArgsConstructor  // Costruttore con tutti i parametri
@NoArgsConstructor   // Costruttore vuoto per deserializzazione JSON
public class AuthResponse {

    /**
     * Token JWT per l'autenticazione delle richieste successive
     *
     * Formato: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJpb0B0ZXN0Lml0..."
     * Il client deve inviarlo nell'header: Authorization: Bearer <token>
     * Valido per 24 ore (configurabile)
     */
    private String token;

    /**
     * Email dell'utente autenticato
     *
     * Corrisponde al campo 'subject' del token JWT
     * Utilizzata come identificatore univoco dell'utente
     */
    private String email;

    /**
     * Nome completo dell'utente (firstName + " " + lastName)
     *
     * Comodo per mostrare il nome nell'interfaccia utente
     * Esempio: "Mario Rossi"
     */
    private String fullName;

    /**
     * Messaggio descrittivo dell'operazione
     *
     * Esempi:
     * - "Login effettuato con successo"
     * - "Registrazione completata"
     * - "Utente autenticato"
     *
     * Utile per feedback all'utente o debugging
     */
    private String message;
}