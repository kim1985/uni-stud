package com.uni.unistud.service;

import com.uni.unistud.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface StudentService {

    // === METODI CRUD ===
    StudentDTO updateStudent(Long studentId, StudentDTO studentDTO);
    StudentDTO getStudentById(Long studentId);
    List<StudentDTO> getAllStudents();
    List<StudentDTO> getStudentsWithCourses();
    void deleteStudent(Long studentId);

    // === METODI ISCRIZIONE CORSI ===
    StudentDTO enrollStudentInCourse(StudentCourseDTO studentCourseDTO);
    StudentDTO unenrollStudentFromCourse(StudentCourseDTO studentCourseDTO);

    // 4. METODI AUTENTICAZIONE
    /**
     * Registra un nuovo studente nel sistema
     * @param request dati di registrazione (firstName, lastName, email, password)
     * @return risposta con token JWT generato
     */
    AuthResponse register(AuthRequest request);

    /**
     * Autentica uno studente esistente
     * @param request credenziali di accesso (email, password)
     * @return risposta con token JWT generato
     */
    AuthResponse login(AuthRequest request);
}
