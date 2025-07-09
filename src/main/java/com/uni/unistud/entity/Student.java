package com.uni.unistud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "first_name"),
        @UniqueConstraint(columnNames = "last_name"),
        @UniqueConstraint(columnNames = "email")
})
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SEQUENCE se uso postgresql o oracle pag 261
    private Long studentId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    @Size(max = 50)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }
}
