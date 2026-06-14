package ua.opnu.labwork2.instructor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Дані для створення викладача")
public record InstructorCreateRequest(
        @Schema(description = "Ім'я викладача", example = "Марія", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must contain from 2 to 50 characters")
        String firstName,

        @Schema(description = "Прізвище викладача", example = "Іваненко", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must contain from 2 to 50 characters")
        String lastName,

        @Schema(description = "Унікальний email викладача", example = "m.ivanenko@op.edu.ua", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 100, message = "Email cannot be longer than 100 characters")
        String email,

        @Schema(description = "Спеціалізація викладача", example = "Java та Spring Boot", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Specialization is required")
        @Size(min = 3, max = 100, message = "Specialization must contain from 3 to 100 characters")
        String specialization
) {
}
