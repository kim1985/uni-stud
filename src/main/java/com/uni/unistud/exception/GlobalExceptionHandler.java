/**
 * Gestore globale delle eccezioni per l'intera applicazione
 * Intercetta tutte le eccezioni non gestite e le converte in risposte HTTP strutturate
 * Garantisce risposte consistenti e codici di stato appropriati per ogni tipo di errore
 */
package com.uni.unistud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce eccezioni di risorse non trovate
     * Converte ResourceNotFoundException in risposta HTTP 404
     *
     * @param ex eccezione catturata
     * @return ResponseEntity con dettagli errore e status 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gestisce eccezioni di risorse duplicate
     * Converte DuplicateResourceException in risposta HTTP 409 (Conflict)
     *
     * @param ex eccezione catturata
     * @return ResponseEntity con dettagli errore e status 409
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Duplicate Resource")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Gestisce eccezioni delle operazioni studente-corso
     * Converte StudentCourseException in risposta HTTP 400 (Bad Request)
     *
     * @param ex eccezione catturata
     * @return ResponseEntity con dettagli errore e status 400
     */
    @ExceptionHandler(StudentCourseException.class)
    public ResponseEntity<ErrorResponse> handleStudentCourseException(StudentCourseException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Student Course Operation Error")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestisce errori di validazione dei dati in input
     * Converte MethodArgumentNotValidException in risposta HTTP 400
     * Raccoglie tutti gli errori di validazione in un'unica risposta strutturata
     *
     * @param ex eccezione di validazione
     * @return ResponseEntity con mappa degli errori di validazione e status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Raccoglie tutti gli errori di validazione in una mappa campo->errore
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Errori di validazione")
                .validationErrors(errors)  // Mappa dettagliata degli errori per campo
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestore di fallback per tutte le altre eccezioni non gestite
     * Evita che errori imprevisti espongano dettagli interni dell'applicazione
     *
     * @param ex qualsiasi eccezione non catturata dai gestori specifici
     * @return ResponseEntity con messaggio generico e status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Si è verificato un errore interno del server")  // Messaggio generico per sicurezza
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gestisce eccezioni di corso al completo
     * Converte CourseFullException in risposta HTTP 409 (Conflict)
     */
    @ExceptionHandler(CourseFullException.class)
    public ResponseEntity<ErrorResponse> handleCourseFullException(CourseFullException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Course Full")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}