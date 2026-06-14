package ua.opnu.labwork2.student.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.opnu.labwork2.course.dto.CourseResponse;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.exception.ApiErrorResponse;
import ua.opnu.labwork2.student.dto.StudentCreateRequest;
import ua.opnu.labwork2.student.dto.StudentResponse;
import ua.opnu.labwork2.student.dto.StudentUpdateRequest;
import ua.opnu.labwork2.student.model.Student;
import ua.opnu.labwork2.student.servise.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(name = "Студенти", description = "Управління студентами та курсами, на які вони записані")
public class StudentController {
    @Autowired
    private StudentService service;

    @Operation(summary = "Створити студента", description = "Створює профіль студента. Перевіряються ім'я, прізвище, унікальний email та дата реєстрації.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студента створено", content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані студента або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Студент з таким email вже існує", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentCreateRequest request) {
        return ResponseEntity.ok(toResponse(service.create(request)));
    }

    @Operation(summary = "Отримати всіх студентів", description = "Повертає список усіх студентів, зареєстрованих у системі.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список студентів отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Отримати студента за id", description = "Повертає профіль студента за ідентифікатором.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студента знайдено", content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Студента не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(service.getById(id)));
    }

    @Operation(summary = "Оновити студента", description = "Оновлює профіль студента. Перевіряються обов'язкові поля, формат email та унікальність email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студента оновлено", content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані студента або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Студента не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Студент з таким email вже існує", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(toResponse(service.update(id, request)));
    }

    @Operation(summary = "Видалити студента", description = "Видаляє студента за ідентифікатором. За наявності пов'язаних записів сервіс може повернути конфлікт.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студента видалено", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Студента не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Студента не можна видалити через пов'язані записи", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted student " + id);
    }

    @Operation(summary = "Отримати курси студента", description = "Повертає всі курси, на які записаний студент.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курси студента отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Студента не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCourses(id).stream()
                .map(Enrollment::getCourse)
                .map(this::toCourseResponse)
                .toList());
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getRegistrationDate()
        );
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
}