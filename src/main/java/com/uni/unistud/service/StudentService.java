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

    // 4. Service semplice - estende StudentService esistente
    // NUOVI METODI per autenticazione
    StudentDTO register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
