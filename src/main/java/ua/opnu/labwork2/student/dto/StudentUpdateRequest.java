package ua.opnu.labwork2.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Дані для оновлення студента")
public record StudentUpdateRequest(
        @Schema(description = "Ім'я студента", example = "Олена", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must contain from 2 to 50 characters")
        String firstName,

        @Schema(description = "Прізвище студента", example = "Коваль", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must contain from 2 to 50 characters")
        String lastName,

        @Schema(description = "Email студента", example = "olena.koval@op.edu.ua", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 100, message = "Email cannot be longer than 100 characters")
        String email,

        @Schema(description = "Дата реєстрації студента", example = "2026-05-10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Registration date is required")
        @PastOrPresent(message = "Registration date cannot be in the future")
        LocalDate registrationDate
) {
}
