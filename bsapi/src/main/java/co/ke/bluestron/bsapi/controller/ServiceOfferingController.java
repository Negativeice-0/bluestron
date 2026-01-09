package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.ServiceOfferingDTO;
import co.ke.bluestron.bsapi.entity.ServiceOffering;
import co.ke.bluestron.bsapi.service.ServiceOfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Service Offerings")
@RestController @RequestMapping("/api/service-offerings")
public class ServiceOfferingController {
    private final ServiceOfferingService service;
    public ServiceOfferingController(ServiceOfferingService service) { this.service = service; }

    @Operation(summary = "List services with optional filter")
    @GetMapping
    public ResponseEntity<List<ServiceOffering>> list(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(status));
    }

    @Operation(summary = "Create a service offering")
    @PostMapping
    public ResponseEntity<ServiceOffering> create(@Valid @RequestBody ServiceOfferingDTO dto) {
        ServiceOffering s = service.create(dto, "system");
        return ResponseEntity.status(201).body(s);
    }

    @Operation(summary = "Get service by slug")
    @GetMapping("/{slug}")
    public ResponseEntity<ServiceOffering> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }
}