/**
 * Eccezione personalizzata per operazioni non valide tra studenti e corsi
 * Utilizzata per gestire errori specifici delle relazioni many-to-many:
 * - Iscrizione a corso già frequentato
 * - Disiscrizione da corso non frequentato
 * Restituisce HTTP 400 BAD REQUEST quando gestita dal GlobalExceptionHandler
 */
package com.uni.unistud.exception;

public class StudentCourseException extends RuntimeException {

    /**
     * Costruttore con messaggio di errore
     * @param message descrizione dell'operazione non valida (es: "Studente già iscritto al corso")
     */
    public StudentCourseException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa originale
     * @param message descrizione dell'errore
     * @param cause eccezione che ha scatenato questo errore
     */
    public StudentCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
