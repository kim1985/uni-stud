// 5. Controller semplice
// AuthController.java
package com.uni.unistud.controller;

import com.uni.unistud.dto.*;
import com.uni.unistud.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller semplice per autenticazione
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;

    /**
     * Registrazione studente
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        StudentDTO student = studentService.register(request);

        // Crea risposta semplice
        AuthResponse response = new AuthResponse();
        response.setEmail(student.getEmail());
        response.setFullName(student.getFirstName() + " " + student.getLastName());
        response.setMessage("Registrazione completata");

        return ResponseEntity.ok(response);
    }

    /**
     * Login studente
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = studentService.login(request);
        return ResponseEntity.ok(response);
    }
}