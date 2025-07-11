package com.uni.unistud.exception;

/**
 * Eccezione per tentativi di iscrizione a corsi che hanno raggiunto la capacità massima
 * Restituisce HTTP 409 CONFLICT quando gestita dal GlobalExceptionHandler
 */
public class CourseFullException extends RuntimeException {

    public CourseFullException(String message) {
        super(message);
    }

    public CourseFullException(String message, Throwable cause) {
        super(message, cause);
    }
}
