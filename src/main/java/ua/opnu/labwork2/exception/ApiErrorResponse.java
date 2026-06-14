package ua.opnu.labwork2.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Єдиний формат відповіді сервера у випадку помилки")
public record ApiErrorResponse(
        @Schema(description = "Час виникнення помилки на сервері", example = "2026-05-10T18:30:00")
        LocalDateTime timestamp,

        @Schema(description = "HTTP-статус помилки", example = "400")
        int status,

        @Schema(description = "Назва HTTP-помилки", example = "Bad Request")
        String error,

        @Schema(description = "Загальне повідомлення про помилку", example = "Validation failed")
        String message,

        @Schema(description = "URL запиту, під час якого виникла помилка", example = "/courses")
        String path,

        @Schema(description = "Помилки валідації окремих полів", example = "{\"title\":\"Course title is required\"}")
        Map<String, String> errors
) {
}
