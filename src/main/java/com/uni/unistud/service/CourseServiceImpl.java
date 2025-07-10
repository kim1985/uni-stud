package com.uni.unistud.service;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.entity.Student;
import com.uni.unistud.exception.ResourceNotFoundException;
import com.uni.unistud.repository.CourseRepository;
import com.uni.unistud.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course student = modelMapper.map(courseDTO, Course.class);
        Course savedCourse = courseRepository.save(student);
        return modelMapper.map(savedCourse, CourseDTO.class);
    }

    @Override
    public CourseDTO getCourseById(Long courseId) {
        Course courseFromDatabase = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId));
        return modelMapper.map(courseFromDatabase, CourseDTO.class);
    }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
        Course courseFromDatabase = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId));

        courseFromDatabase.setTitle(courseDTO.getTitle());
        Course updatedCourse = courseRepository.save(courseFromDatabase);

        return modelMapper.map(updatedCourse, CourseDTO.class);
    }

    @Override
    public String deleteCourse(Long courseId) {
        Course studentFromDatabase = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId));
        courseRepository.delete(studentFromDatabase);
        return "Course "+courseId+" deleted";
    }
}
