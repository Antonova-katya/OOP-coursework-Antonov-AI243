package ua.opnu.labwork2.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.opnu.labwork2.course.repository.CourseRepository;
import ua.opnu.labwork2.enrollment.repository.EnrollmentRepository;
import ua.opnu.labwork2.student.repository.StudentRepository;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(AnalyticsController.BASE_PATH)
public class AnalyticsController {
    public static final String BASE_PATH = "/analytics";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/courses/count")
    public ResponseEntity<Long> coursesCount() {
        return ResponseEntity.ok(courseRepository.count());
    }

    @GetMapping("/students/count")
    public ResponseEntity<Long> studentsCount() {
        return ResponseEntity.ok(studentRepository.count());
    }

    @GetMapping("/enrollments/active")
    public ResponseEntity<Long> active() {
        return ResponseEntity.ok(enrollmentRepository.countByCompletionDateIsNull());
    }

    @GetMapping("/enrollments/completed")
    public ResponseEntity<Long> completed() {
        return ResponseEntity.ok(enrollmentRepository.countByCompletionDateIsNotNull());
    }

    @GetMapping("/courses/by-level")
    public ResponseEntity<Map<String, Long>> byLevel() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : courseRepository.countCoursesByLevel()) {
            String level = row[0] == null ? "UNKNOWN" : row[0].toString();
            Long count = (Long) row[1];
            result.put(level, count);
        }
        return ResponseEntity.ok(result);
    }
}