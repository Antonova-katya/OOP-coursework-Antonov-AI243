package ua.opnu.labwork2.course.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.course.dto.CourseCreateRequest;
import ua.opnu.labwork2.course.dto.CourseUpdateRequest;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.course.repository.CourseRepository;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.enrollment.repository.EnrollmentRepository;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.instructor.model.Instructor;
import ua.opnu.labwork2.instructor.repository.InstructorRepository;
import ua.opnu.labwork2.module.model.CourseModule;
import ua.opnu.labwork2.module.repository.ModuleRepository;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Course create(CourseCreateRequest request) {
        Course course = new Course();
        apply(course, request.title(), request.description(), request.level(), request.createdDate(), request.instructorId());
        return repository.save(course);
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public Course getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    public Course update(Long id, CourseUpdateRequest request) {
        Course course = getById(id);
        apply(course, request.title(), request.description(), request.level(), request.createdDate(), request.instructorId());
        return repository.save(course);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Course", id);
        }
        repository.deleteById(id);
    }

    public List<Course> getByInstructor(Long instructorId) {
        if (!instructorRepository.existsById(instructorId)) {
            throw new ResourceNotFoundException("Instructor", instructorId);
        }
        return repository.findAll()
                .stream()
                .filter(c -> c.getInstructor() != null &&
                        c.getInstructor().getId().equals(instructorId))
                .toList();
    }

    public List<CourseModule> getModules(Long courseId) {
        getById(courseId);
        return moduleRepository.findAll()
                .stream()
                .filter(m -> m.getCourse() != null &&
                        m.getCourse().getId().equals(courseId))
                .toList();
    }

    public List<Enrollment> getStudents(Long courseId) {
        getById(courseId);
        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getCourse() != null &&
                        e.getCourse().getId().equals(courseId))
                .toList();
    }

    private void apply(Course course, String title, String description, String level,
                       java.time.LocalDate createdDate, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", instructorId));
        course.setTitle(title);
        course.setDescription(description);
        course.setLevel(level);
        course.setCreatedDate(createdDate);
        course.setInstructor(instructor);
    }
}