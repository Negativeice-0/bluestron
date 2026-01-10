package co.ke.bluestron.bsapi.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating courses.
 */
public record CourseDTO(
    @NotBlank @Size(max = 150) String slug,
    @NotBlank @Size(max = 200) String title,
    String shortDescription,
    String fullDescription,
    List<@NotBlank String> learningOutcomes,
    String thumbnailUrl,
    @NotNull Long categoryId,
    @Pattern(regexp = "draft|published|archived") String status
) {}
