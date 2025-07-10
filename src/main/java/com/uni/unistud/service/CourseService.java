package com.uni.unistud.service;

import com.uni.unistud.dto.CourseDTO;
import jakarta.validation.Valid;

public interface CourseService {
    CourseDTO createCourse(@Valid CourseDTO courseDTO);

    CourseDTO getCourseById(Long courseId);

    CourseDTO updateCourse(Long courseId, @Valid CourseDTO courseDTO);

    String deleteCourse(Long courseId);
}
