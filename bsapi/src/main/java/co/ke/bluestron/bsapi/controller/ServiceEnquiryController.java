package co.ke.bluestron.bsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.ServiceEnquiryDTO;
import co.ke.bluestron.bsapi.entity.ServiceEnquiry;
import co.ke.bluestron.bsapi.service.ServiceEnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Service Enquiries")
@RestController @RequestMapping("/api/service-enquiries")
public class ServiceEnquiryController {
    private final ServiceEnquiryService service;
    public ServiceEnquiryController(ServiceEnquiryService s) { this.service = s; }

    @Operation(summary = "Submit a service enquiry")
    @PostMapping
    public ResponseEntity<ServiceEnquiry> submit(@Valid @RequestBody ServiceEnquiryDTO dto) {
        ServiceEnquiry e = service.submit(dto, "system");
        return ResponseEntity.status(201).body(e);
    }
}