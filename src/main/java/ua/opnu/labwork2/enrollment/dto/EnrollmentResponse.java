package ua.opnu.labwork2.enrollment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Вихідна модель запису студента на курс")
public record EnrollmentResponse(
        @Schema(description = "Ідентифікатор запису на курс", example = "1")
        Long id,
        @Schema(description = "Ідентифікатор студента", example = "1")
        Long studentId,
        @Schema(description = "Ідентифікатор курсу", example = "3")
        Long courseId,
        @Schema(description = "Дата запису студента на курс", example = "2026-05-10")
        LocalDate enrollmentDate,
        @Schema(description = "Дата завершення курсу студентом", example = "2026-06-20")
        LocalDate completionDate,
        @Schema(description = "Поточний прогрес проходження курсу у відсотках", example = "85")
        Integer progress
) {
}