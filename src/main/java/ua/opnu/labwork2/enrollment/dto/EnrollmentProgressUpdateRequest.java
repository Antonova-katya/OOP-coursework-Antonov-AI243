package ua.opnu.labwork2.enrollment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Дані для оновлення прогресу проходження курсу")
public record EnrollmentProgressUpdateRequest(
        @Schema(description = "Поточний прогрес проходження курсу у відсотках", example = "85", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Progress is required")
        @Min(value = 0, message = "Progress cannot be less than 0")
        @Max(value = 100, message = "Progress cannot be greater than 100")
        Integer progress
) {
}