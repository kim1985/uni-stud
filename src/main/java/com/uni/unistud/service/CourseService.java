package com.uni.unistud.service;

import com.uni.unistud.dto.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long courseId, CourseDTO courseDTO);
    CourseDTO getCourseById(Long courseId);
    Page<CourseDTO> getAllCourses(Pageable pageable);
    Page<CourseDTO> getCoursesWithStudents(Pageable pageable);
    Page<CourseDTO> searchCoursesByTitle(String title, Pageable pageable);
    void deleteCourse(Long courseId);
}
