package co.ke.bluestron.bsapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BlogPostDTO(
    @NotBlank @Size(max = 150) String slug,
    @NotBlank @Size(max = 200) String title,
    @Size(max = 500) String excerpt,
    @NotBlank @Size(max = 50000) String content,
    @Size(max = 500) String featuredImage,
    @Size(max = 100) String author,
    LocalDate publishedDate,
    @Pattern(regexp = "draft|published|archived") String status
) {}