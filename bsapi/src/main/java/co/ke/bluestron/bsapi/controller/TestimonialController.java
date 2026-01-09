package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.TestimonialDTO;
import co.ke.bluestron.bsapi.entity.Testimonial;
import co.ke.bluestron.bsapi.service.TestimonialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Testimonials")
@RestController @RequestMapping("/api/testimonials")
public class TestimonialController {
    private final TestimonialService service;
    public TestimonialController(TestimonialService service) { this.service = service; }

    @Operation(summary = "List testimonials with optional filter")
    @GetMapping
    public ResponseEntity<List<Testimonial>> list(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(status));
    }

    @Operation(summary = "Create a testimonial")
    @PostMapping
    public ResponseEntity<Testimonial> create(@Valid @RequestBody TestimonialDTO dto) {
        Testimonial t = service.create(dto, "system");
        return ResponseEntity.status(201).body(t);
    }
}