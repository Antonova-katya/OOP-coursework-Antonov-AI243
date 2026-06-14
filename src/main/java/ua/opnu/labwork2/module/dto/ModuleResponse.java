package ua.opnu.labwork2.module.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Вихідна модель навчального модуля курсу")
public record ModuleResponse(
        @Schema(description = "Ідентифікатор модуля", example = "1")
        Long id,
        @Schema(description = "Назва модуля", example = "Вступ до Java")
        String title,
        @Schema(description = "Опис тем і матеріалів модуля", example = "Базові поняття мови Java, типи даних та перша програма.")
        String description,
        @Schema(description = "Порядковий номер модуля в курсі", example = "1")
        Integer orderIndex,
        @Schema(description = "Ідентифікатор курсу, до якого належить модуль", example = "3")
        Long courseId
) {
}