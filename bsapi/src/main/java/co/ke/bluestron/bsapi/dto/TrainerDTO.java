package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TrainerDTO(
    @NotBlank @Size(max = 100) String slug,
    @NotBlank @Size(max = 150) String fullName,
    @Size(max = 100) String title,
    @Size(max = 5000) String bio,
    @Size(max = 500) String photoUrl,
    @Size(max = 100) String twitter,
    @Size(max = 100) String linkedin,
    @Size(max = 100) String github,
    @Pattern(regexp = "active|inactive") String status
) {}