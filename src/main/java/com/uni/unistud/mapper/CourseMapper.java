package com.uni.unistud.mapper;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.entity.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {
    public CourseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());

        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            dto.setStudents(course.getStudents().stream()
                    .map(student -> {
                        StudentMapper studentMapper = new StudentMapper();
                        return studentMapper.toDTOWithoutCourses(student);
                    })
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    public Course toEntity(CourseDTO dto) {
        if (dto == null) return null;

        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setTitle(dto.getTitle());

        return course;
    }

    public List<CourseDTO> toDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO toDTOWithoutStudents(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());

        return dto;
    }
}
