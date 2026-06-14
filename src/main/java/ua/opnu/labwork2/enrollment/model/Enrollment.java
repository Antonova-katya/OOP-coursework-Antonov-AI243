package ua.opnu.labwork2.enrollment.model;

import jakarta.persistence.*;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.student.model.Student;
import java.time.LocalDate;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate enrollmentDate;
    private LocalDate completionDate;
    private Integer progress;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Enrollment() {
    }

    public Enrollment(Long id, LocalDate enrollmentDate, LocalDate completionDate, Integer progress) {
        this.id = id;

        this.enrollmentDate = enrollmentDate;
        this.completionDate = completionDate;
        this.progress = progress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public Integer getProgress() {
        return progress;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}