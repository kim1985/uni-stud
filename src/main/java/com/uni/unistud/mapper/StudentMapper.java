/**
 * Mapper per la conversione tra entità Student e DTO StudentDTO
 *
 * Responsabilità:
 * - Conversione bidirezionale Entity ↔ DTO
 * - Gestione relazioni many-to-many senza riferimenti circolari
 * - Mapping ottimizzato per diversi use case
 * - Integrazione con CourseMapper per relazioni cross-entity
 */
package com.uni.unistud.mapper;

import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    /**
     * Converte Student -> StudentDTO includendo i corsi associati
     *
     * Utilizzo:
     * - GET /api/students/{id} (singolo studente con corsi)
     * - GET /api/students/with-courses (lista completa con relazioni)
     * - Operazioni di iscrizione/disiscrizione (risposta con aggiornamenti)
     *
     * Performance N+1:
     * Richiede che Student.courses sia caricato con JOIN FETCH per evitare N+1.
     * Senza JOIN FETCH: 1 query per studenti + N query per corsi (lento)
     * Con JOIN FETCH: 1 sola query per tutto (veloce)
     */
    public StudentDTO toDTO(Student student) {
        if (student == null) return null;

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());

        // Include corsi se presenti, senza i loro studenti (evita cicli infiniti)
        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            dto.setCourses(student.getCourses().stream()
                    .map(course -> {
                        CourseMapper courseMapper = new CourseMapper();
                        return courseMapper.toDTOWithoutStudents(course);
                    })
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    /**
     * Converte StudentDTO -> Student per operazioni di persistenza
     *
     * Caratteristiche:
     * - Include solo campi diretti (no relazioni)
     * - Relazioni gestite tramite service layer
     * - ID null per create, valorizzato per update
     */
    public Student toEntity(StudentDTO dto) {
        if (dto == null) return null;

        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());

        return student;
    }

    /**
     * Converte lista di Student -> lista di StudentDTO
     *
     * Stream Processing:
     * - Mapping funzionale per performance
     * - Utilizzato per endpoint paginati
     * - Support per operazioni di ricerca
     */
    public List<StudentDTO> toDTOList(List<Student> students) {
        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Student -> StudentDTO SENZA includere corsi
     *
     * Ottimizzazioni:
     * - Evita lazy loading quando non necessario
     * - Utilizzato per prevenire riferimenti circolari
     * - Performance ottimale per scenari senza relazioni
     *
     * Scenari d'uso:
     * - CourseDTO.students (info base studente)
     * - Liste dove i corsi non servono
     * - Dashboard con overview studenti
     */
    public StudentDTO toDTOWithoutCourses(Student student) {
        if (student == null) return null;

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());

        return dto;
    }
}