package com.uni.unistud.exception;

/**
 * Eccezione personalizzata per risorse non trovate nel sistema
 * Utilizzata quando si tenta di accedere a entità (studenti, corsi) che non esistono nel database
 * Restituisce HTTP 404 NOT FOUND quando gestita dal GlobalExceptionHandler
 */
public class ResourceNotFoundException  extends RuntimeException{
    /**
     * Costruttore con messaggio di errore
     * @param message descrizione dettagliata dell'errore (es: "Studente non trovato con ID: 123")
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa originale
     * @param message descrizione dell'errore
     * @param cause eccezione che ha scatenato questo errore
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
