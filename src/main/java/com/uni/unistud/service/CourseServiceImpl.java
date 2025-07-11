package com.uni.unistud.service;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.exception.DuplicateResourceException;
import com.uni.unistud.exception.ResourceNotFoundException;
import com.uni.unistud.mapper.CourseMapper;
import com.uni.unistud.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        if (courseRepository.existsByTitle(courseDTO.getTitle())) {
            throw new DuplicateResourceException("Corso con titolo '" + courseDTO.getTitle() + "' già esistente");
        }

        Course course = courseMapper.toEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDTO(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + courseId));

        if (!existingCourse.getTitle().equals(courseDTO.getTitle()) &&
                courseRepository.existsByTitle(courseDTO.getTitle())) {
            throw new DuplicateResourceException("Titolo già in uso da un altro corso");
        }

        existingCourse.setTitle(courseDTO.getTitle());

        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDTO(updatedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findByIdWithStudents(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + courseId));
        return courseMapper.toDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDTOList(courses);
    }

    @Override
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Corso non trovato con ID: " + courseId);
        }
        courseRepository.deleteById(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesWithStudents() {
        List<Course> courses = courseRepository.findAllWithStudents();
        return courseMapper.toDTOList(courses);
    }
}
