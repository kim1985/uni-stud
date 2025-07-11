/**
 * Mapper per la conversione tra entità Course e DTO CourseDTO
 *
 * Responsabilità:
 * - Conversione bidirezionale Entity ↔ DTO
 * - Gestione relazioni many-to-many evitando riferimenti circolari
 * - Mapping ottimizzato per diversi scenari (con/senza studenti)
 * - Support per operazioni batch e conversioni di liste
 */
package com.uni.unistud.mapper;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.entity.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    /**
     * Converte Course -> CourseDTO includendo gli studenti associati
     *
     * Utilizzo:
     * - Endpoint /api/courses/{id} (dettagli corso completi)
     * - Endpoint /api/courses/with-students (lista con relazioni)
     *
     * Performance N+1:
     * Richiede che Course.students sia caricato con JOIN FETCH per evitare N+1.
     * Senza JOIN FETCH: 1 query per corsi + N query per studenti (lento)
     * Con JOIN FETCH: 1 sola query per tutto (veloce)
     */
    public CourseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());

        // Include studenti se presenti, senza i loro corsi (evita cicli infiniti)
        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            dto.setStudents(course.getStudents().stream()
                    .map(student -> {
                        StudentMapper studentMapper = new StudentMapper();
                        return studentMapper.toDTOWithoutCourses(student);
                    })
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    /**
     * Converte CourseDTO -> Course per operazioni di persistenza
     *
     * Caratteristiche:
     * - Mapping semplice dei campi base (ID, title)
     * - Non include relazioni (gestite dal service tramite metodi helper)
     * - Utilizzato per operazioni CREATE/UPDATE
     */
    public Course toEntity(CourseDTO dto) {
        if (dto == null) return null;

        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setTitle(dto.getTitle());

        return course;
    }

    /**
     * Converte lista di Course -> lista di CourseDTO
     *
     * Utilizzo:
     * - Endpoint di paginazione
     * - Operazioni di ricerca e filtro
     * - Conversioni batch per performance
     */
    public List<CourseDTO> toDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Course -> CourseDTO SENZA includere studenti
     *
     * Ottimizzazioni:
     * - Evita caricamento lazy quando non necessario
     * - Utilizzato per prevenire riferimenti circolari
     * - Riduce dimensioni JSON response
     *
     * Scenari d'uso:
     * - StudentDTO.courses (include solo info base del corso)
     * - Liste dove le relazioni non servono
     */
    public CourseDTO toDTOWithoutStudents(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());

        return dto;
    }
}