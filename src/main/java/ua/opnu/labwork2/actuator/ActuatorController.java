package ua.opnu.labwork2.actuator;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ActuatorController.BASE_PATH)
public class ActuatorController {
    public static final String BASE_PATH = "/actuator";

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "labwork-2",
                "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> metrics() {
        return ResponseEntity.ok(Map.of(
                "availableMetrics", List.of(
                        "courses.count",
                        "students.count",
                        "enrollments.active",
                        "enrollments.completed",
                        "courses.byLevel"
                )
        ));
    }

    @GetMapping(value = "/prometheus", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> prometheus() {
        String metrics = """
                # HELP application_up Application health status
                # TYPE application_up gauge
                application_up 1
                # HELP application_info Application information
                # TYPE application_info gauge
                application_info{service="labwork-2"} 1
                """;
        return ResponseEntity.ok(metrics);
    }
}