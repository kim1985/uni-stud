// RegisterRequest.java
package com.uni.unistud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Nome obbligatorio")
    private String firstName;

    @NotBlank(message = "Cognome obbligatorio")
    private String lastName;

    @Email(message = "Email non valida")
    @NotBlank(message = "Email obbligatoria")
    private String email;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "Password minimo 6 caratteri")
    private String password;
}