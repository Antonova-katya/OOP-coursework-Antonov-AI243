package ua.opnu.labwork2.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Дані для створення нового онлайн-курсу")
public record CourseCreateRequest(
        @Schema(description = "Назва курсу", example = "Java для початківців", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Course title is required")
        @Size(min = 3, max = 150, message = "Course title must contain from 3 to 150 characters")
        String title,

        @Schema(description = "Опис змісту курсу", example = "Повний вступний курс з Java, ООП, колекціями та базовими принципами Spring.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Course description is required")
        @Size(min = 20, max = 2000, message = "Course description must contain from 20 to 2000 characters")
        String description,

        @Schema(description = "Рівень складності курсу", example = "BEGINNER", allowableValues = {"BEGINNER", "INTERMEDIATE", "ADVANCED"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Course level is required")
        @Pattern(regexp = "BEGINNER|INTERMEDIATE|ADVANCED", message = "Course level must be BEGINNER, INTERMEDIATE or ADVANCED")
        String level,

        @Schema(description = "Дата створення курсу", example = "2026-05-10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Created date is required")
        @PastOrPresent(message = "Created date cannot be in the future")
        LocalDate createdDate,

        @Schema(description = "Ідентифікатор викладача, який веде курс", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Instructor id is required")
        @Positive(message = "Instructor id must be positive")
        Long instructorId
) {
}
