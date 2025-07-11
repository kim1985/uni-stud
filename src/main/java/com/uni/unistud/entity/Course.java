/**
 * Entità JPA che rappresenta un corso nel database
 * Mappata sulla tabella "courses" con relazione many-to-many verso Student
 */
package com.uni.unistud.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity                                    // JPA: questa classe è un'entità del database
@Table(name = "courses")                   // JPA: mappa sulla tabella "courses"
@Data                                      // Lombok: genera getter, setter, toString, equals, hashCode
@AllArgsConstructor                        // Lombok: costruttore con tutti i parametri
@NoArgsConstructor                         // Lombok: costruttore vuoto (obbligatorio per JPA)
@EqualsAndHashCode(exclude = "students")   // Lombok: esclude "students" da equals/hashCode (evita loop infiniti)
@ToString(exclude = "students")            // Lombok: esclude "students" da toString (evita loop infiniti)
public class Course {

    /**
     * ID univoco del corso (chiave primaria)
     * Generato automaticamente dal database
     */
    @Id                                           // JPA: questo campo è la chiave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: auto-incremento gestito dal database
    private Long courseId;

    /**
     * Titolo del corso - obbligatorio e univoco
     */
    @Column(nullable = false)                     // JPA: colonna NOT NULL nel database
    private String title;

    /**
     * Studenti iscritti a questo corso
     * Relazione many-to-many: un corso può avere molti studenti
     * LAZY: gli studenti vengono caricati solo quando servono (migliori performance)
     */
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)  // JPA: relazione many-to-many, LAZY = carica solo quando necessario
    private Set<Student> students = new HashSet<>();

    /**
     * Capacità massima del corso - numero massimo di studenti che possono iscriversi
     * Valore di default: 50 studenti
     * Se null o 0 = capacità illimitata
     */
    @Column(nullable = false)
    private Integer maxCapacity = 50;

    /**
     * Verifica se il corso ha ancora posti disponibili
     * @return true se ci sono posti liberi, false se è al completo
     */
    public boolean hasAvailableSpots() {
        if (maxCapacity == null || maxCapacity <= 0) {
            return true; // Capacità illimitata
        }
        return students.size() < maxCapacity;
    }

    /**
     * Calcola il numero di posti disponibili
     * @return numero di posti liberi (-1 se capacità illimitata)
     */
    public int getAvailableSpots() {
        if (maxCapacity == null || maxCapacity <= 0) {
            return -1; // Capacità illimitata
        }
        return maxCapacity - students.size();
    }

    /**
     * Verifica se il corso è al completo
     * @return true se non ci sono più posti disponibili
     */
    public boolean isFull() {
        return !hasAvailableSpots();
    }
}