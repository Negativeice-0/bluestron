package co.ke.bluestron.bsapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CourseInstanceDTO(
    @NotNull Long courseId,
    @NotBlank @Pattern(regexp = "online|in_person") String mode,
    LocalDate startDate,
    LocalDate endDate,
    @Min(1) Integer capacity,
    @Pattern(regexp = "open|closed|full") String status,
    Long venueId  // optional - only for in_person
) {}