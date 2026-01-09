package co.ke.bluestron.bsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.RegistrationDTO;
import co.ke.bluestron.bsapi.entity.Registration;
import co.ke.bluestron.bsapi.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Registrations")
@RestController @RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService service;
    public RegistrationController(RegistrationService s) { this.service = s; }

    @Operation(summary = "Register for a course/cohort")
    @PostMapping
    public ResponseEntity<Registration> register(@Valid @RequestBody RegistrationDTO dto) {
        Registration r = service.register(dto, "system");
        return ResponseEntity.status(201).body(r);
    }
}
