package com.uni.unistud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
}
