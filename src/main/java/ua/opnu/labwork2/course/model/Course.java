package ua.opnu.labwork2.course.model;

import jakarta.persistence.*;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.instructor.model.Instructor;
import ua.opnu.labwork2.module.model.CourseModule;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String level;
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @OneToMany(mappedBy = "course")
    private List<CourseModule> modules;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

    public Course() {
    }

    public Course(Long id, String title, String description, String level, LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}