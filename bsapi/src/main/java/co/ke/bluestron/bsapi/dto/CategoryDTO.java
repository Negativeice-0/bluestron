package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating categories. Validation ensures clean inputs.
 */
public record CategoryDTO(
    @NotBlank @Size(max = 100) String slug,
    @NotBlank @Size(max = 150) String name,
    @Size(max = 10000) String description,
    @NotBlank @Size(max = 20) String status
) {}
