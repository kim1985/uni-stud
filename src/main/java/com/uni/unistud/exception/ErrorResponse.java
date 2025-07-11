/**
 * Classe che rappresenta la struttura standardizzata delle risposte di errore
 * Garantisce che tutti gli errori abbiano un formato JSON consistente
 * Utilizzata da GlobalExceptionHandler per costruire risposte uniformi
 */
package com.uni.unistud.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /** Timestamp dell'errore per debugging e logging */
    private LocalDateTime timestamp;

    /** Codice di stato HTTP numerico (404, 409, 400, 500, etc.) */
    private int status;

    /** Descrizione breve del tipo di errore ("Resource Not Found", "Validation Error", etc.) */
    private String error;

    /** Messaggio dettagliato dell'errore specifico per l'utente */
    private String message;

    /**
     * Mappa degli errori di validazione per campo (opzionale)
     * Utilizzata solo per errori di validazione @Valid
     * Formato: {"email": "Email non valida", "firstName": "Nome obbligatorio"}
     */
    private Map<String, String> validationErrors;
}