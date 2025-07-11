/**
 * Entità JPA che rappresenta uno studente nel database
 * Mappata sulla tabella "students" con relazione many-to-many verso Course
 */
package com.uni.unistud.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data                                      // Lombok: genera getter, setter, toString, equals, hashCode
@AllArgsConstructor                        // Lombok: costruttore con tutti i parametri
@NoArgsConstructor                         // Lombok: costruttore vuoto (obbligatorio per JPA)
@Entity                                    // JPA: questa classe è un'entità del database
@Table(name = "students")                  // JPA: mappa sulla tabella "students"
@EqualsAndHashCode(exclude = "courses")    // Lombok: esclude "courses" da equals/hashCode (evita loop infiniti)
@ToString(exclude = "courses")             // Lombok: esclude "courses" da toString (evita loop infiniti)
public class Student {

    /**
     * ID univoco dello studente (chiave primaria)
     * Generato automaticamente dal database
     */
    @Id                                           // JPA: questo campo è la chiave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: auto-incremento (per PostgreSQL/Oracle usare SEQUENCE)
    private Long studentId;

    /**
     * Nome dello studente - campo obbligatorio
     */
    @Column(nullable = false)                     // JPA: colonna NOT NULL nel database
    private String firstName;

    /**
     * Cognome dello studente - campo obbligatorio
     */
    @Column(nullable = false)                     // JPA: colonna NOT NULL nel database
    private String lastName;

    /**
     * Email dello studente - obbligatoria e univoca
     * Usata come identificatore alternativo
     */
    @Column(nullable = false, unique = true)      // JPA: colonna NOT NULL e UNIQUE (con indice automatico)
    private String email;

    // 1. Aggiungi solo il campo password all'entità Student esistente
    // NUOVO CAMPO - password criptata
    @Column(nullable = true) // nullable per mantenere compatibilità con dati esistenti
    private String password;

    /**
     * Corsi a cui lo studente è iscritto
     * Relazione many-to-many: uno studente può frequentare molti corsi
     * Questa è la parte "proprietaria" della relazione
     *
     * LAZY: i corsi vengono caricati solo quando servono (risparmia memoria)
     *
     * CASCADE spiegato:
     * - PERSIST: quando salvo uno studente con corsi nuovi, salva automaticamente anche i corsi
     * - MERGE: quando aggiorno uno studente, aggiorna anche i corsi collegati
     * - NO DELETE: se cancello uno studente, i corsi rimangono (altri studenti potrebbero frequentarli)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // JPA: LAZY = carica solo se richiesto, CASCADE = propaga salvataggio/aggiornamento ai corsi
    @JoinTable(
            name = "student_courses",                    // JPA: nome della tabella di collegamento
            joinColumns = @JoinColumn(name = "student_id"),      // JPA: colonna che referenzia Student
            inverseJoinColumns = @JoinColumn(name = "course_id") // JPA: colonna che referenzia Course
    )
    private Set<Course> courses = new HashSet<>();

    /**
     * Aggiunge un corso a questo studente
     * Gestisce correttamente la relazione bidirezionale
     *
     * @param course il corso da aggiungere
     */
    public void addCourse(Course course) {
        courses.add(course);                // Aggiunge il corso a questo studente
        course.getStudents().add(this);     // Aggiunge questo studente al corso
    }

    /**
     * Rimuove un corso da questo studente
     * Gestisce correttamente la relazione bidirezionale
     *
     * @param course il corso da rimuovere
     */
    public void removeCourse(Course course) {
        courses.remove(course);             // Rimuove il corso da questo studente
        course.getStudents().remove(this);  // Rimuove questo studente dal corso
    }
}