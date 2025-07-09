package com.uni.unistud.service;

import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.entity.Student;
import com.uni.unistud.repository.CourseRepository;
import com.uni.unistud.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Student student = studentRepository.findById(studentId).orElseThrow(RuntimeException::new);
        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public void addCourseToStudent(Long studentId, Long courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (studentOptional.isPresent() && courseOptional.isPresent()) {
            Student student = studentOptional.get();
            Course course = courseOptional.get();
            student.addCourse(course);
            studentRepository.save(student);
        }

    }
}
