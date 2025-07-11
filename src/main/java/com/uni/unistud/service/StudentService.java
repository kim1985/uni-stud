package com.uni.unistud.service;

import com.uni.unistud.dto.StudentCourseDTO;
import com.uni.unistud.dto.StudentDTO;
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
}
