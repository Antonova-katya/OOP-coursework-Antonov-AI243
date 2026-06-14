package ua.opnu.labwork2.course.api;

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
import ua.opnu.labwork2.course.dto.CourseCreateRequest;
import ua.opnu.labwork2.course.dto.CourseResponse;
import ua.opnu.labwork2.course.dto.CourseUpdateRequest;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.course.servise.CourseService;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.exception.ApiErrorResponse;
import ua.opnu.labwork2.module.dto.ModuleResponse;
import ua.opnu.labwork2.module.model.CourseModule;
import ua.opnu.labwork2.student.dto.StudentResponse;
import ua.opnu.labwork2.student.model.Student;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Tag(name = "Курси", description = "Управління онлайн-курсами, їх викладачами, модулями та студентами")
public class CourseController {

    @Autowired
    private CourseService service;

    @Operation(summary = "Створити курс", description = "Створює новий курс. Перевіряється назва, опис, рівень складності, дата створення та існування викладача.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успішно створено", content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані курсу або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Викладача для курсу не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Конфлікт бізнес-правил під час створення курсу", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseCreateRequest request) {
        return ResponseEntity.ok(toResponse(service.create(request)));
    }

    @Operation(summary = "Отримати всі курси", description = "Повертає список усіх курсів, зареєстрованих у системі.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список курсів отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Отримати курс за id", description = "Повертає один курс за ідентифікатором. Якщо курс не існує, повертається помилка 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс знайдено", content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(service.getById(id)));
    }

    @Operation(summary = "Оновити курс", description = "Оновлює дані курсу. Перевіряється валідність DTO, існування курсу та викладача.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс оновлено", content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані курсу або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Курс або викладача не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Конфлікт бізнес-правил під час оновлення курсу", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseUpdateRequest request) {
        return ResponseEntity.ok(toResponse(service.update(id, request)));
    }

    @Operation(summary = "Видалити курс", description = "Видаляє курс за ідентифікатором. Якщо курс має пов'язані дані, сервіс може повернути конфлікт бізнес-правил.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс видалено", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Курс не можна видалити через пов'язані записи", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted course " + id);
    }

    @Operation(summary = "Отримати курси викладача", description = "Повертає всі курси, які веде конкретний викладач.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список курсів викладача отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Викладача не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseResponse>> getByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(service.getByInstructor(instructorId).stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Отримати модулі курсу", description = "Повертає навчальні модулі конкретного курсу.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модулі курсу отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ModuleResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/modules")
    public ResponseEntity<List<ModuleResponse>> getModules(@PathVariable Long id) {
        return ResponseEntity.ok(service.getModules(id).stream().map(this::toModuleResponse).toList());
    }

    @Operation(summary = "Отримати студентів курсу", description = "Повертає студентів, які зараховані на конкретний курс.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студентів курсу отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudents(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudents(id).stream()
                .map(Enrollment::getStudent)
                .map(this::toStudentResponse)
                .toList());
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getCreatedDate(),
                course.getInstructor() == null ? null : course.getInstructor().getId()
        );
    }

    private ModuleResponse toModuleResponse(CourseModule module) {
        return new ModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex(),
                module.getCourse() == null ? null : module.getCourse().getId()
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