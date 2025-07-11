package com.uni.unistud.service;

import com.uni.unistud.dto.CourseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long courseId, CourseDTO courseDTO);
    CourseDTO getCourseById(Long courseId);
    List<CourseDTO> getAllCourses();
    void deleteCourse(Long courseId);
    List<CourseDTO> getCoursesWithStudents();
}
