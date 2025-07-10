package com.uni.unistud.service;

import com.uni.unistud.dto.StudentDTO;
import jakarta.validation.Valid;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO getStudentById(Long studentId);

    void addCourseToStudent(Long studentId, Long courseId);

    StudentDTO updateStudent(Long studentId, StudentDTO studentDTO);

    String deleteStudent(Long studentId);
}
