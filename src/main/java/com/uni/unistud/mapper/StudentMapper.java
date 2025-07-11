package com.uni.unistud.mapper;

import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {
    public StudentDTO toDTO(Student student) {
        if (student == null) return null;

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());

        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            dto.setCourses(student.getCourses().stream()
                    .map(course -> {
                        CourseMapper courseMapper = new CourseMapper();
                        return courseMapper.toDTOWithoutStudents(course);
                    })
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        if (dto == null) return null;

        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());

        return student;
    }

    public List<StudentDTO> toDTOList(List<Student> students) {
        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO toDTOWithoutCourses(Student student) {
        if (student == null) return null;

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());

        return dto;
    }
}
