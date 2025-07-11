package com.uni.unistud.controller;

import com.uni.unistud.dto.StudentCourseDTO;
import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long studentId,
                                                    @Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(studentId, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long studentId) {
        StudentDTO student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/with-courses")
    public ResponseEntity<List<StudentDTO>> getStudentsWithCourses() {
        List<StudentDTO> students = studentService.getStudentsWithCourses();
        return ResponseEntity.ok(students);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enroll")
    public ResponseEntity<StudentDTO> enrollStudentInCourse(@Valid @RequestBody StudentCourseDTO studentCourseDTO) {
        StudentDTO student = studentService.enrollStudentInCourse(studentCourseDTO);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/unenroll")
    public ResponseEntity<StudentDTO> unenrollStudentFromCourse(@Valid @RequestBody StudentCourseDTO studentCourseDTO) {
        StudentDTO student = studentService.unenrollStudentFromCourse(studentCourseDTO);
        return ResponseEntity.ok(student);
    }
}
