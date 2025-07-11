package com.uni.unistud.dto;

import com.uni.unistud.entity.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long courseId;
    @NotBlank(message = "Il titolo del corso è obbligatorio")
    private String title;
    private Set<StudentDTO> students;
}
