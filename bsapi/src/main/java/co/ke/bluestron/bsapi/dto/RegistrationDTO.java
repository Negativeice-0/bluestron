package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationDTO(
    @NotNull Long courseId,
    Long courseInstanceId,
    @NotBlank @Size(max = 150) String fullName,
    @NotBlank @Email @Size(max = 150) String email,
    @Size(max = 50) String phone,
    @Size(max = 150) String organization,
    @Size(max = 100) String role,
    @Pattern(regexp = "online|invoice") String paymentOption
) {}
