package com.uni.unistud.dto;

import com.uni.unistud.entity.Student;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long lessonId;
    private String title;
    private Set<Student> students = new HashSet<>();
}
