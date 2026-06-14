package ua.opnu.labwork2.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.opnu.labwork2.course.dto.CourseResponse;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.course.repository.CourseRepository;
import ua.opnu.labwork2.student.dto.StudentResponse;
import ua.opnu.labwork2.student.model.Student;
import ua.opnu.labwork2.student.repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping(TextSearchController.BASE_PATH)
public class TextSearchController {
    public static final String BASE_PATH = "/search";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> searchCourses(@RequestParam String query) {
        return ResponseEntity.ok(courseRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(this::toCourseResponse)
                .toList());
    }

    @GetMapping("/courses/advanced")
    public ResponseEntity<List<CourseResponse>> advanced(@RequestParam String query, @RequestParam String level) {
        return ResponseEntity.ok(courseRepository.searchByQueryAndLevel(query, level)
                .stream()
                .map(this::toCourseResponse)
                .toList());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> searchStudents(@RequestParam String query) {
        return ResponseEntity.ok(studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        query,
                        query,
                        query
                )
                .stream()
                .map(this::toStudentResponse)
                .toList());
    }

    private CourseResponse toCourseResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getCreatedDate(),
                course.getInstructor() == null ? null : course.getInstructor().getId()
        );
    }

    private StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getRegistrationDate()
        );
    }
}