package ua.opnu.labwork2.enrollment.api;

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
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork2.enrollment.dto.EnrollmentCreateRequest;
import ua.opnu.labwork2.enrollment.dto.EnrollmentProgressUpdateRequest;
import ua.opnu.labwork2.enrollment.dto.EnrollmentResponse;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.enrollment.servise.EnrollmentService;
import ua.opnu.labwork2.exception.ApiErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@Tag(name = "Записи на курси", description = "Управління записами студентів на онлайн-курси та прогресом навчання")
public class EnrollmentController {

    @Autowired
    private EnrollmentService service;

    @Operation(summary = "Записати студента на курс", description = "Створює запис студента на курс. Перевіряється існування студента і курсу, унікальність запису та коректність дат.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запис створено", content = @Content(schema = @Schema(implementation = EnrollmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані запису або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Студента або курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Студент вже записаний на цей курс", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody EnrollmentCreateRequest request) {
        return ResponseEntity.ok(toResponse(service.create(request)));
    }

    @Operation(summary = "Отримати всі записи", description = "Повертає список усіх записів студентів на курси.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список записів отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnrollmentResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Отримати запис за id", description = "Повертає запис студента на курс за ідентифікатором.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запис знайдено", content = @Content(schema = @Schema(implementation = EnrollmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Запис не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(service.getById(id)));
    }

    @Operation(summary = "Оновити прогрес", description = "Оновлює відсоток проходження курсу для конкретного запису. Значення прогресу має бути від 0 до 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Прогрес оновлено", content = @Content(schema = @Schema(implementation = EnrollmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректне значення прогресу або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Запис не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}/progress")
    public ResponseEntity<EnrollmentResponse> updateProgress(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentProgressUpdateRequest request) {
        return ResponseEntity.ok(toResponse(service.updateProgress(id, request)));
    }

    @Operation(summary = "Скасувати запис", description = "Видаляє запис студента на курс за ідентифікатором.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запис видалено", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Запис не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted enrollment " + id);
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent() == null ? null : enrollment.getStudent().getId(),
                enrollment.getCourse() == null ? null : enrollment.getCourse().getId(),
                enrollment.getEnrollmentDate(),
                enrollment.getCompletionDate(),
                enrollment.getProgress()
        );
    }
}