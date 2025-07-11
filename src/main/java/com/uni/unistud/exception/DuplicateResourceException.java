/**
 * Eccezione personalizzata per risorse duplicate nel sistema
 * Utilizzata quando si tenta di creare/aggiornare entità con valori che violano vincoli di unicità
 * (es: email studente, titolo corso già esistenti)
 * Restituisce HTTP 409 CONFLICT quando gestita dal GlobalExceptionHandler
 */
package com.uni.unistud.exception;

public class DuplicateResourceException extends RuntimeException {

    /**
     * Costruttore con messaggio di errore
     * @param message descrizione del conflitto (es: "Email già utilizzata da un altro studente")
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa originale
     * @param message descrizione del conflitto
     * @param cause eccezione che ha scatenato questo errore (es: SQLException per constraint violation)
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
