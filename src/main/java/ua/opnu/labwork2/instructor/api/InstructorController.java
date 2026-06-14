package ua.opnu.labwork2.instructor.api;

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
import ua.opnu.labwork2.exception.ApiErrorResponse;
import ua.opnu.labwork2.instructor.dto.InstructorCreateRequest;
import ua.opnu.labwork2.instructor.dto.InstructorResponse;
import ua.opnu.labwork2.instructor.dto.InstructorUpdateRequest;
import ua.opnu.labwork2.instructor.model.Instructor;
import ua.opnu.labwork2.instructor.servise.InstructorService;

import java.util.List;

@RestController
@RequestMapping("/instructors")
@Tag(name = "Викладачі", description = "Управління викладачами та курсами, які вони проводять")
public class InstructorController {

    @Autowired
    private InstructorService service;

    @Operation(summary = "Створити викладача", description = "Створює профіль викладача. Перевіряються ім'я, прізвище, унікальний email та спеціалізація.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викладача створено", content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані викладача або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Викладач з таким email вже існує", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<InstructorResponse> create(@Valid @RequestBody InstructorCreateRequest request) {
        return ResponseEntity.ok(toResponse(service.create(request)));
    }

    @Operation(summary = "Отримати всіх викладачів", description = "Повертає список усіх викладачів системи.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список викладачів отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InstructorResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<InstructorResponse>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Отримати викладача за id", description = "Повертає профіль викладача за ідентифікатором.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викладача знайдено", content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Викладача не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(service.getById(id)));
    }

    @Operation(summary = "Оновити викладача", description = "Оновлює профіль викладача. Перевіряються обов'язкові поля, формат email та унікальність email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викладача оновлено", content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані викладача або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Викладача не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Викладач з таким email вже існує", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InstructorUpdateRequest request) {
        return ResponseEntity.ok(toResponse(service.update(id, request)));
    }

    @Operation(summary = "Видалити викладача", description = "Видаляє викладача за ідентифікатором. Якщо викладач має курси, може бути повернутий конфлікт.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викладача видалено", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Викладача не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Викладача не можна видалити через пов'язані курси", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted instructor " + id);
    }

    @Operation(summary = "Отримати курси викладача", description = "Повертає курси, які закріплені за конкретним викладачем.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курси викладача отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Викладача не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCourses(id).stream().map(this::toCourseResponse).toList());
    }

    private InstructorResponse toResponse(Instructor instructor) {
        return new InstructorResponse(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                instructor.getSpecialization()
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