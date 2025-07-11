// 2. DTO semplici per autenticazione
// LoginRequest.java
package com.uni.unistud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email non valida")
    @NotBlank(message = "Email obbligatoria")
    private String email;

    @NotBlank(message = "Password obbligatoria")
    private String password;
}