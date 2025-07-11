/**
 * DTO per rappresentare uno studente nelle comunicazioni con il client
 * Utilizzato per registrazione, aggiornamento dati e visualizzazione studenti
 */
package com.uni.unistud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data                // Lombok: genera boilerplate code automaticamente
@AllArgsConstructor  // Costruttore con tutti i campi (utile per test e builder pattern)
@NoArgsConstructor   // Costruttore vuoto necessario per framework (Spring, Jackson JSON)
public class StudentDTO {

    /**
     * ID univoco dello studente (chiave primaria)
     * - Auto-generato dal database per nuovi studenti
     * - Utilizzato per operazioni CRUD e iscrizioni ai corsi
     */
    private Long studentId;

    /**
     * Nome dello studente - campo obbligatorio
     * Utilizzato per ricerche e visualizzazioni
     */
    @NotBlank(message = "Il nome è obbligatorio")  // Bean Validation: stringa non vuota
    private String firstName;

    /**
     * Cognome dello studente - campo obbligatorio
     * Utilizzato per ordinamenti e ricerche
     */
    @NotBlank(message = "Il cognome è obbligatorio")  // Bean Validation: stringa non vuota
    private String lastName;

    /**
     * Email dello studente - campo obbligatorio e univoco nel sistema
     * Validazioni:
     * - @Email: formato email valido
     * - @NotBlank: campo obbligatorio
     * - Unicità verificata a livello service
     * Utilizzata come identificatore alternativo per future funzionalità
     */
    @Email(message = "Email non valida")           // Bean Validation: formato email
    @NotBlank(message = "L'email è obbligatoria")  // Bean Validation: stringa non vuota
    private String email;

    /**
     * Set dei corsi a cui lo studente è iscritto
     * - Popolato negli endpoint "with-courses"
     * - Evita riferimenti circolari: CourseDTO non contiene studenti
     * - Relazione many-to-many gestita tramite StudentCourseDTO
     */
    private Set<CourseDTO> courses;
}