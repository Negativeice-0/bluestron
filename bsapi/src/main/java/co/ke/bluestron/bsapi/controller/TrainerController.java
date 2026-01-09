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

import co.ke.bluestron.bsapi.dto.TrainerDTO;
import co.ke.bluestron.bsapi.entity.Trainer;
import co.ke.bluestron.bsapi.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Trainers")
@RestController @RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService service;
    public TrainerController(TrainerService service) { this.service = service; }

    @Operation(summary = "List trainers with optional filter")
    @GetMapping
    public ResponseEntity<List<Trainer>> list(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(status));
    }

    @Operation(summary = "Create a trainer")
    @PostMapping
    public ResponseEntity<Trainer> create(@Valid @RequestBody TrainerDTO dto) {
        Trainer t = service.create(dto, "system");
        return ResponseEntity.status(201).body(t);
    }

    @Operation(summary = "Get trainer by slug")
    @GetMapping("/{slug}")
    public ResponseEntity<Trainer> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }
}