/**
 * DTO per rappresentare un corso nelle comunicazioni con il client
 * Utilizzato per richieste/risposte di creazione, aggiornamento e visualizzazione corsi
 * Non include logica di business, solo dati e validazioni di formato
 */
package com.uni.unistud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data                // Genera automaticamente getter, setter, toString, equals, hashCode
@AllArgsConstructor  // Genera costruttore con tutti i parametri
@NoArgsConstructor   // Genera costruttore vuoto (richiesto per JSON deserializzazione)
public class CourseDTO {

    /**
     * ID univoco del corso (chiave primaria)
     * - Null per nuovi corsi (generato dal database)
     * - Valorizzato per corsi esistenti negli endpoint PUT/GET/DELETE
     */
    private Long courseId;

    /**
     * Titolo del corso - campo obbligatorio e univoco nel sistema
     * Validazioni:
     * - @NotBlank: non può essere null, vuoto o solo spazi
     * - Unicità verificata a livello service
     */
    @NotBlank(message = "Il titolo del corso è obbligatorio")  // Validazione Bean Validation
    private String title;

    /**
     * Set degli studenti iscritti al corso
     * - Utilizzato negli endpoint "with-students"
     * - Evita riferimenti circolari: StudentDTO non contiene corsi
     * - Conversione gestita dal CourseMapper
     */
    private Set<StudentDTO> students;
}