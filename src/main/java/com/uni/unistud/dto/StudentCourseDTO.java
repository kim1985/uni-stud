package com.uni.unistud.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseDTO {
    @NotNull(message = "L'ID dello studente è obbligatorio")
    private Long studentId;

    @NotNull(message = "L'ID del corso è obbligatorio")
    private Long courseId;
}
