package ua.opnu.labwork2.enrollment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Дані для створення запису студента на курс")
public record EnrollmentCreateRequest(
        @Schema(description = "Ідентифікатор студента", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Student id is required")
        @Positive(message = "Student id must be positive")
        Long studentId,

        @Schema(description = "Ідентифікатор курсу", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Course id is required")
        @Positive(message = "Course id must be positive")
        Long courseId,

        @Schema(description = "Дата запису студента на курс", example = "2026-05-10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Enrollment date is required")
        @PastOrPresent(message = "Enrollment date cannot be in the future")
        LocalDate enrollmentDate,

        @Schema(description = "Дата завершення курсу студентом", example = "2026-06-20")
        LocalDate completionDate,

        @Schema(description = "Поточний прогрес проходження курсу у відсотках", example = "25", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Progress is required")
        @Min(value = 0, message = "Progress cannot be less than 0")
        @Max(value = 100, message = "Progress cannot be greater than 100")
        Integer progress
) {
}