package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TestimonialDTO(
    @NotBlank @Size(max = 150) String authorName,
    @Size(max = 100) String authorRole,
    @Size(max = 100) String authorCompany,
    @NotBlank @Size(max = 5000) String content,
    @Size(max = 500) String photoUrl,
    @Min(1) @Max(5) Integer rating,
    @Pattern(regexp = "active|inactive") String status
) {}