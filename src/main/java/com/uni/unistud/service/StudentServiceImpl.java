package com.uni.unistud.service;

import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.entity.Student;
import com.uni.unistud.exception.ResourceNotFoundException;
import com.uni.unistud.repository.CourseRepository;
import com.uni.unistud.repository.StudentRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = modelMapper.map(studentDTO, Student.class);
        Student savedStudent = studentRepository.save(student);
        return modelMapper.map(savedStudent, StudentDTO.class);
    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        Student studentFromDatabase = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        return modelMapper.map(studentFromDatabase, StudentDTO.class);
    }

    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO studentDTO) {
        Student studentFromDatabase = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));

        studentFromDatabase.setFirstName(studentDTO.getFirstName());
        studentFromDatabase.setLastName(studentDTO.getLastName());
        studentFromDatabase.setEmail(studentDTO.getEmail());
        Student updatedStudent = studentRepository.save(studentFromDatabase);

        return modelMapper.map(updatedStudent, StudentDTO.class);

    }

    @Override
    public String deleteStudent(Long studentId) {
        Student studentFromDatabase = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        studentRepository.delete(studentFromDatabase);
        return "Student "+studentId+" deleted";
    }

    @Override
    public void addCourseToStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId));
        student.addCourse(course);
        studentRepository.save(student);

    }
}
