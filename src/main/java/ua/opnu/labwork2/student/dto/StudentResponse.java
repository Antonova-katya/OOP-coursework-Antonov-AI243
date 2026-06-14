package ua.opnu.labwork2.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Вихідна модель студента")
public record StudentResponse(
        @Schema(description = "Ідентифікатор студента", example = "1")
        Long id,
        @Schema(description = "Ім'я студента", example = "Іван")
        String firstName,
        @Schema(description = "Прізвище студента", example = "Петренко")
        String lastName,
        @Schema(description = "Email студента", example = "ivan.petrenko@op.edu.ua")
        String email,
        @Schema(description = "Дата реєстрації", example = "2026-05-10")
        LocalDate registrationDate
) {
}
