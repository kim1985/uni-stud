/**
 * Controller REST per la gestione dei corsi
 * Fornisce endpoint per CRUD completo e operazioni di ricerca con paginazione
 */
package com.uni.unistud.controller;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * Crea un nuovo corso
     *
     * Chiamata: POST http://localhost:8080/api/courses
     * Body JSON:
     * {
     *   "title": "Programmazione Java Avanzata"
     * }
     */
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    /**
     * Aggiorna un corso esistente
     *
     * Chiamata: PUT http://localhost:8080/api/courses/1
     * Body JSON:
     * {
     *   "title": "Programmazione Java Expert"
     * }
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long courseId,
                                                  @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Recupera un singolo corso con i suoi studenti
     *
     * Chiamata: GET http://localhost:8080/api/courses/1
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId) {
        CourseDTO course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    /**
     * Recupera tutti i corsi con paginazione (senza studenti)
     *
     * Chiamata: GET http://localhost:8080/api/courses?page=0&size=10&sort=title
     */
    @GetMapping
    public ResponseEntity<Page<CourseDTO>> getAllCourses(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<CourseDTO> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Recupera tutti i corsi con i loro studenti (paginato)
     *
     * Chiamata: GET http://localhost:8080/api/courses/with-students?page=0&size=5
     */
    @GetMapping("/with-students")
    public ResponseEntity<Page<CourseDTO>> getCoursesWithStudents(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<CourseDTO> courses = courseService.getCoursesWithStudents(pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Cerca corsi per titolo con paginazione
     *
     * Chiamata: GET http://localhost:8080/api/courses/search?title=java&page=0&size=5
     */
    @GetMapping("/search")
    public ResponseEntity<Page<CourseDTO>> searchCoursesByTitle(
            @RequestParam String title,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<CourseDTO> courses = courseService.searchCoursesByTitle(title, pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Elimina un corso
     *
     * Chiamata: DELETE http://localhost:8080/api/courses/1
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}