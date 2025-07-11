package com.uni.unistud.repository;

import com.uni.unistud.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.studentId = :studentId")
    Optional<Student> findByIdWithCourses(@Param("studentId") Long studentId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses")
    List<Student> findAllWithCourses();

    boolean existsByEmail(String email);
}
