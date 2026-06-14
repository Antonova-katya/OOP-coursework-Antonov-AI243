package ua.opnu.labwork2.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Вихідна модель курсу")
public record CourseResponse(
        @Schema(description = "Ідентифікатор курсу", example = "1")
        Long id,
        @Schema(description = "Назва курсу", example = "Java для початківців")
        String title,
        @Schema(description = "Опис курсу", example = "Повний вступний курс з Java та Spring.")
        String description,
        @Schema(description = "Рівень складності", example = "BEGINNER")
        String level,
        @Schema(description = "Дата створення", example = "2026-05-10")
        LocalDate createdDate,
        @Schema(description = "Ідентифікатор викладача", example = "1")
        Long instructorId
) {
}