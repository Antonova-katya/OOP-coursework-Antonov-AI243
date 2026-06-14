package ua.opnu.labwork2.enrollment.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.course.repository.CourseRepository;
import ua.opnu.labwork2.enrollment.dto.EnrollmentCreateRequest;
import ua.opnu.labwork2.enrollment.dto.EnrollmentProgressUpdateRequest;
import ua.opnu.labwork2.enrollment.dto.EnrollmentUpdateRequest;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.enrollment.repository.EnrollmentRepository;
import ua.opnu.labwork2.exception.BadRequestException;
import ua.opnu.labwork2.exception.CourseEnrollmentException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.student.model.Student;
import ua.opnu.labwork2.student.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Enrollment create(EnrollmentCreateRequest request) {
        validateCompletionDate(request.enrollmentDate(), request.completionDate());
        if (repository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw new CourseEnrollmentException("Student is already enrolled in this course");
        }

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.courseId()));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(request.enrollmentDate());
        enrollment.setCompletionDate(request.completionDate());
        enrollment.setProgress(request.progress());
        return repository.save(enrollment);
    }

    public List<Enrollment> getAll() {
        return repository.findAll();
    }

    public Enrollment getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
    }

    public Enrollment update(Long id, EnrollmentUpdateRequest request) {
        validateCompletionDate(request.enrollmentDate(), request.completionDate());
        Enrollment enrollment = getById(id);
        if (repository.existsByStudentIdAndCourseIdAndIdNot(request.studentId(), request.courseId(), id)) {
            throw new CourseEnrollmentException("Student is already enrolled in this course");
        }

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.courseId()));

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(request.enrollmentDate());
        enrollment.setCompletionDate(request.completionDate());
        enrollment.setProgress(request.progress());
        return repository.save(enrollment);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment", id);
        }
        repository.deleteById(id);
    }

    public Enrollment updateProgress(Long id, EnrollmentProgressUpdateRequest request) {
        Enrollment enrollment = getById(id);
        enrollment.setProgress(request.progress());
        return repository.save(enrollment);
    }

    private void validateCompletionDate(LocalDate enrollmentDate, LocalDate completionDate) {
        if (completionDate != null && enrollmentDate != null && completionDate.isBefore(enrollmentDate)) {
            throw new BadRequestException("Completion date cannot be before enrollment date");
        }
    }
}