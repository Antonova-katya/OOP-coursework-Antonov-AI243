package ua.opnu.labwork2.module.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Дані для створення навчального модуля курсу")
public record ModuleCreateRequest(
        @Schema(description = "Назва модуля", example = "Вступ до Java", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Module title is required")
        @Size(min = 3, max = 150, message = "Module title must contain from 3 to 150 characters")
        String title,

        @Schema(description = "Опис тем і матеріалів модуля", example = "Базові поняття мови Java, типи даних та перша програма.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Module description is required")
        @Size(min = 10, max = 1000, message = "Module description must contain from 10 to 1000 characters")
        String description,

        @Schema(description = "Порядковий номер модуля в курсі", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Order index is required")
        @Positive(message = "Order index must be positive")
        Integer orderIndex,

        @Schema(description = "Ідентифікатор курсу, до якого належить модуль", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Course id is required")
        @Positive(message = "Course id must be positive")
        Long courseId
) {
}