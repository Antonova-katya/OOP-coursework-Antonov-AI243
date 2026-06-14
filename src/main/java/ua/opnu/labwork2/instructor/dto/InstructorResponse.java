package ua.opnu.labwork2.instructor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Вихідна модель викладача")
public record InstructorResponse(
        @Schema(description = "Ідентифікатор викладача", example = "1")
        Long id,
        @Schema(description = "Ім'я викладача", example = "Марія")
        String firstName,
        @Schema(description = "Прізвище викладача", example = "Іваненко")
        String lastName,
        @Schema(description = "Email викладача", example = "m.ivanenko@op.edu.ua")
        String email,
        @Schema(description = "Спеціалізація викладача", example = "Java та Spring Boot")
        String specialization
) {
}