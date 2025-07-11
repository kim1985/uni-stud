/**
 * DTO per rappresentare un corso nelle comunicazioni con il client
 * Utilizzato per richieste/risposte di creazione, aggiornamento e visualizzazione corsi
 * Non include logica di business, solo dati e validazioni di formato
 */
package com.uni.unistud.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    /**
     * Capacità massima del corso
     * Validazioni:
     * - @Min(1): almeno 1 posto (0 o null = illimitato)
     * - @Max(500): massimo ragionevole per un corso
     */
    @Min(value = 1, message = "La capacità deve essere almeno 1 (0 per illimitato)")
    @Max(value = 500, message = "La capacità massima consentita è 500")
    private Integer maxCapacity;

    // Campi derivati per facilitare il frontend

    /**
     * Numero attuale di studenti iscritti
     * Calcolato automaticamente dal mapper
     */
    private Integer currentEnrollment;

    /**
     * Numero di posti disponibili
     * -1 se capacità illimitata
     */
    private Integer availableSpots;

    /**
     * Indica se il corso è al completo
     */
    private Boolean isFull;
}