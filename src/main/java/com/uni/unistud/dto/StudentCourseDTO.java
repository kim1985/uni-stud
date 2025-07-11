/**
 * DTO specializzato per operazioni di iscrizione/disiscrizione ai corsi
 * Rappresenta la relazione many-to-many tra Student e Course
 *
 * Utilizzato per:
 * - POST /api/students/enroll (iscrizione a corso)
 * - POST /api/students/unenroll (disiscrizione da corso)
 */
package com.uni.unistud.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // Lombok: getter, setter, toString, equals, hashCode
@AllArgsConstructor  // Costruttore con studentId e courseId
@NoArgsConstructor   // Costruttore vuoto per JSON binding
public class StudentCourseDTO {

    /**
     * ID dello studente da iscrivere/disiscrivere
     * - Deve corrispondere a uno studente esistente
     * - Utilizzato per recuperare l'entità Student dal database
     */
    @NotNull(message = "L'ID dello studente è obbligatorio")  // Bean Validation: non null
    private Long studentId;

    /**
     * ID del corso per l'iscrizione/disiscrizione
     * - Deve corrispondere a un corso esistente
     * - Utilizzato per verificare/modificare la relazione student-course
     */
    @NotNull(message = "L'ID del corso è obbligatorio")  // Bean Validation: non null
    private Long courseId;
}