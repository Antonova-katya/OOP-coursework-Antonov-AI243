package ua.opnu.labwork2.instructor.model;

import jakarta.persistence.*;
import ua.opnu.labwork2.course.model.Course;
import java.util.List;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String specialization;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    public Instructor() {
    }

    public Instructor(Long id, String firstName, String lastName, String email, String specialization) {
        this.id = id;

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}