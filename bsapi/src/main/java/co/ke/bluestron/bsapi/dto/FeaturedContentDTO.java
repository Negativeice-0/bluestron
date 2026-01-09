package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FeaturedContentDTO(
    @NotBlank @Size(max = 50) String section,
    @NotBlank @Size(max = 100) String contentType, // course/blog/service
    @NotNull Long contentId,
    @Size(max = 200) String customTitle,
    String customDescription,
    @NotNull @Min(0) Integer displayOrder,
    @Pattern(regexp = "active|inactive") String status
) {}