package ua.opnu.labwork2.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Дані для оновлення онлайн-курсу")
public record CourseUpdateRequest(
        @Schema(description = "Оновлена назва курсу", example = "Java Core та Spring", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Course title is required")
        @Size(min = 3, max = 150, message = "Course title must contain from 3 to 150 characters")
        String title,

        @Schema(description = "Оновлений опис курсу", example = "Курс охоплює Java Core, принципи ООП, JPA та створення REST API.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Course description is required")
        @Size(min = 20, max = 2000, message = "Course description must contain from 20 to 2000 characters")
        String description,

        @Schema(description = "Рівень складності курсу", example = "INTERMEDIATE", allowableValues = {"BEGINNER", "INTERMEDIATE", "ADVANCED"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Course level is required")
        @Pattern(regexp = "BEGINNER|INTERMEDIATE|ADVANCED", message = "Course level must be BEGINNER, INTERMEDIATE or ADVANCED")
        String level,

        @Schema(description = "Дата створення або оновлення запису курсу", example = "2026-05-10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Created date is required")
        @PastOrPresent(message = "Created date cannot be in the future")
        LocalDate createdDate,

        @Schema(description = "Ідентифікатор викладача курсу", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Instructor id is required")
        @Positive(message = "Instructor id must be positive")
        Long instructorId
) {
}