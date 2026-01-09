package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceEnquiryDTO(
    Long serviceId,
    @NotBlank @Size(max = 150) String fullName,
    @NotBlank @Email @Size(max = 150) String email,
    @Size(max = 50) String phone,
    @Size(max = 150) String company,
    @NotBlank @Size(max = 5000) String message
) {}