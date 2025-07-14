/**
 * Controller REST per la gestione degli studenti
 * Fornisce endpoint per CRUD completo, iscrizioni ai corsi e ricerca
 */
package com.uni.unistud.controller;

import com.uni.unistud.dto.StudentCourseDTO;
import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * Aggiorna un studente esistente
     *
     * Chiamata: PUT http://localhost:8080/api/students/15
     * Header: Authorization: Bearer <token>
     * Body JSON:
     * {
     *   "firstName": "Anna Maria",
     *   "lastName": "Verdi",
     *   "email": "annamaria.verdi@university.it"
     * }
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long studentId,
                                                    @Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(studentId, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Recupera un singolo studente con i suoi corsi
     *
     * Chiamata: GET http://localhost:8080/api/students/15
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long studentId) {
        StudentDTO student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    /**
     * Recupera tutti gli studenti (senza corsi)
     *
     * Chiamata: GET http://localhost:8080/api/students
     * Header: Authorization: Bearer <token>
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Recupera tutti gli studenti con i loro corsi
     *
     * Chiamata: GET http://localhost:8080/api/students/with-courses
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/with-courses")
    public ResponseEntity<List<StudentDTO>> getStudentsWithCourses() {
        List<StudentDTO> students = studentService.getStudentsWithCourses();
        return ResponseEntity.ok(students);
    }

    /**
     * Elimina uno studente
     *
     * Chiamata: DELETE http://localhost:8080/api/students/15
     * Header: Authorization: Bearer <token>
     */
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Iscrive uno studente a un corso
     *
     * Chiamata: POST http://localhost:8080/api/students/enroll
     * Header: Authorization: Bearer <token>
     * Body JSON:
     * {
     *   "studentId": 15,
     *   "courseId": 1
     * }
     */
    @PostMapping("/enroll")
    public ResponseEntity<StudentDTO> enrollStudentInCourse(@Valid @RequestBody StudentCourseDTO studentCourseDTO) {
        StudentDTO student = studentService.enrollStudentInCourse(studentCourseDTO);
        return ResponseEntity.ok(student);
    }

    /**
     * Disiscrive uno studente da un corso
     *
     * Chiamata: POST http://localhost:8080/api/students/unenroll
     * Header: Authorization: Bearer <token>
     * Body JSON:
     * {
     *   "studentId": 15,
     *   "courseId": 1
     * }
     */
    @PostMapping("/unenroll")
    public ResponseEntity<StudentDTO> unenrollStudentFromCourse(@Valid @RequestBody StudentCourseDTO studentCourseDTO) {
        StudentDTO student = studentService.unenrollStudentFromCourse(studentCourseDTO);
        return ResponseEntity.ok(student);
    }
}