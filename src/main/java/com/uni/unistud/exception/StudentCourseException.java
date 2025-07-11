package com.uni.unistud.exception;

public class StudentCourseException extends RuntimeException {
    public StudentCourseException(String message) {
        super(message);
    }

    public StudentCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
