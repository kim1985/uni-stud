package com.uni.unistud.controller;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO savedCourseDTO = courseService.createCourse(courseDTO);
        return new ResponseEntity<>(savedCourseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId) {
        CourseDTO courseDTO = courseService.getCourseById(courseId);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long courseId, @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updateCourse = courseService.updateCourse(courseId, courseDTO);
        return new ResponseEntity<>(updateCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        String status = courseService.deleteCourse(courseId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
