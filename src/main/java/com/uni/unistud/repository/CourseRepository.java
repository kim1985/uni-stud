package com.uni.unistud.repository;

import com.uni.unistud.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.courseId = :courseId")
    Optional<Course> findByIdWithStudents(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students")
    List<Course> findAllWithStudents();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    Page<Course> findAllWithStudents(Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<Course> findByTitle(String title);

    boolean existsByTitle(String title);
}
