package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ServiceOfferingDTO(
    @NotBlank @Size(max = 100) String slug,
    @NotBlank @Size(max = 150) String name,
    @Size(max = 5000) String description,
    @Size(max = 50) String icon,
    @Pattern(regexp = "active|inactive") String status
) {}