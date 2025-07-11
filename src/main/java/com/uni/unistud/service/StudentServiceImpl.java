package com.uni.unistud.service;

import com.uni.unistud.dto.StudentCourseDTO;
import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.entity.Student;
import com.uni.unistud.exception.DuplicateResourceException;
import com.uni.unistud.exception.ResourceNotFoundException;
import com.uni.unistud.exception.StudentCourseException;
import com.uni.unistud.mapper.StudentMapper;
import com.uni.unistud.repository.CourseRepository;
import com.uni.unistud.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new DuplicateResourceException("Studente con email " + studentDTO.getEmail() + " già esistente");
        }

        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentId));

        if (!existingStudent.getEmail().equals(studentDTO.getEmail()) &&
                studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new DuplicateResourceException("Email già in uso da un altro studente");
        }

        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setEmail(studentDTO.getEmail());

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long studentId) {
        Student student = studentRepository.findByIdWithCourses(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentId));
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toDTOList(students);
    }

    @Override
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Studente non trovato con ID: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public StudentDTO enrollStudentInCourse(StudentCourseDTO studentCourseDTO) {
        Student student = studentRepository.findByIdWithCourses(studentCourseDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentCourseDTO.getStudentId()));

        Course course = courseRepository.findById(studentCourseDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + studentCourseDTO.getCourseId()));

        if (student.getCourses().contains(course)) {
            throw new StudentCourseException("Lo studente è già iscritto a questo corso");
        }

        student.addCourse(course);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    public StudentDTO unenrollStudentFromCourse(StudentCourseDTO studentCourseDTO) {
        Student student = studentRepository.findByIdWithCourses(studentCourseDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentCourseDTO.getStudentId()));

        Course course = courseRepository.findById(studentCourseDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + studentCourseDTO.getCourseId()));

        if (!student.getCourses().contains(course)) {
            throw new StudentCourseException("Lo studente non è iscritto a questo corso");
        }

        student.removeCourse(course);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsWithCourses() {
        List<Student> students = studentRepository.findAllWithCourses();
        return studentMapper.toDTOList(students);
    }
}
