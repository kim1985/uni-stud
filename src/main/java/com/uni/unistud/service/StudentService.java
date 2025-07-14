package com.uni.unistud.service;

import com.uni.unistud.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(Long studentId, StudentDTO studentDTO);

    StudentDTO getStudentById(Long studentId);

    List<StudentDTO> getAllStudents();
    void deleteStudent(Long studentId);
    StudentDTO enrollStudentInCourse(StudentCourseDTO studentCourseDTO);
    StudentDTO unenrollStudentFromCourse(StudentCourseDTO studentCourseDTO);
    List<StudentDTO> getStudentsWithCourses();

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
