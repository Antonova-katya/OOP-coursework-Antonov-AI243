package ua.opnu.labwork2.module.api;

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
import ua.opnu.labwork2.exception.ApiErrorResponse;
import ua.opnu.labwork2.module.dto.ModuleCreateRequest;
import ua.opnu.labwork2.module.dto.ModuleResponse;
import ua.opnu.labwork2.module.dto.ModuleUpdateRequest;
import ua.opnu.labwork2.module.model.CourseModule;
import ua.opnu.labwork2.module.servise.ModuleService;

import java.util.List;

@RestController
@Tag(name = "Модулі", description = "Управління навчальними модулями онлайн-курсів")
public class ModuleController {

    @Autowired
    private ModuleService service;

    @Operation(summary = "Створити модуль", description = "Створює навчальний модуль у межах курсу. Перевіряється існування курсу та унікальність порядкового номера модуля в курсі.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модуль створено", content = @Content(schema = @Schema(implementation = ModuleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані модуля або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Модуль з таким порядковим номером вже існує в курсі", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/modules")
    public ResponseEntity<ModuleResponse> create(@Valid @RequestBody ModuleCreateRequest request) {
        return ResponseEntity.ok(toResponse(service.create(request)));
    }

    @Operation(summary = "Отримати всі модулі", description = "Повертає список усіх навчальних модулів.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список модулів отримано", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ModuleResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/modules")
    public ResponseEntity<List<ModuleResponse>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Отримати модуль за id", description = "Повертає навчальний модуль за ідентифікатором.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модуль знайдено", content = @Content(schema = @Schema(implementation = ModuleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Модуль не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/modules/{id}")
    public ResponseEntity<ModuleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(service.getById(id)));
    }

    @Operation(summary = "Оновити модуль", description = "Оновлює дані навчального модуля. Перевіряється валідність DTO, існування курсу та унікальність порядкового номера.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модуль оновлено", content = @Content(schema = @Schema(implementation = ModuleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані модуля або помилки валідації", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Модуль або курс не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Модуль з таким порядковим номером вже існує в курсі", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/modules/{id}")
    public ResponseEntity<ModuleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ModuleUpdateRequest request) {
        return ResponseEntity.ok(toResponse(service.update(id, request)));
    }

    @Operation(summary = "Видалити модуль", description = "Видаляє навчальний модуль за ідентифікатором.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модуль видалено", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Модуль не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/modules/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted module " + id);
    }

    @Operation(summary = "Додати модуль до курсу", description = "Прив'язує існуючий модуль до курсу за ідентифікаторами курсу та модуля.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модуль додано до курсу", content = @Content(schema = @Schema(implementation = ModuleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Курс або модуль не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "У курсі вже є модуль з таким порядковим номером", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/courses/{id}/modules/{moduleId}")
    public ResponseEntity<ModuleResponse> addModule(@PathVariable Long id, @PathVariable Long moduleId) {
        return ResponseEntity.ok(toResponse(service.addToCourse(id, moduleId)));
    }

    @Operation(summary = "Видалити модуль з курсу", description = "Видаляє зв'язок між модулем і курсом без видалення самого модуля.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модуль видалено з курсу", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Курс, модуль або зв'язок модуля з курсом не знайдено", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/courses/{id}/modules/{moduleId}")
    public ResponseEntity<String> removeModule(@PathVariable Long id, @PathVariable Long moduleId) {
        service.removeFromCourse(id, moduleId);
        return ResponseEntity.ok("Removed module " + moduleId + " from course " + id);
    }

    private ModuleResponse toResponse(CourseModule module) {
        return new ModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex(),
                module.getCourse() == null ? null : module.getCourse().getId()
        );
    }
}