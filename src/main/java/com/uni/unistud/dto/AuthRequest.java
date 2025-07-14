package com.uni.unistud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO unificato per richieste di autenticazione
 * Utilizzato sia per login che per registrazione
 *
 * Per LOGIN: serve solo email e password
 * Per REGISTRAZIONE: servono tutti i campi
 */
@Data
public class AuthRequest {

    /**
     * Nome dello studente - obbligatorio solo per registrazione
     * Per login può essere null
     */
    private String firstName;

    /**
     * Cognome dello studente - obbligatorio solo per registrazione
     * Per login può essere null
     */
    private String lastName;

    /**
     * Email - obbligatoria sia per login che registrazione
     */
    @Email(message = "Email non valida")
    @NotBlank(message = "Email obbligatoria")
    private String email;

    /**
     * Password - obbligatoria sia per login che registrazione
     */
    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "Password minimo 6 caratteri")
    private String password;

    /**
     * Helper per determinare se è una richiesta di registrazione
     * @return true se ha nome e cognome (registrazione), false se solo email/password (login)
     */
    public boolean isRegistration() {
        return firstName != null && !firstName.trim().isEmpty() &&
                lastName != null && !lastName.trim().isEmpty();
    }
}