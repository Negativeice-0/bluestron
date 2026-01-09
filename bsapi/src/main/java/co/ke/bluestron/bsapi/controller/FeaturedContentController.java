package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.FeaturedContentDTO;
import co.ke.bluestron.bsapi.entity.FeaturedContent;
import co.ke.bluestron.bsapi.service.FeaturedContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Featured Content")
@RestController @RequestMapping("/api/featured-content")
public class FeaturedContentController {
    private final FeaturedContentService service;
    public FeaturedContentController(FeaturedContentService service) { this.service = service; }

    @Operation(summary = "List featured content with optional filters")
    @GetMapping
    public ResponseEntity<List<FeaturedContent>> list(@RequestParam(required = false) String section,
                                                      @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(section, status));
    }

    @Operation(summary = "Create featured content")
    @PostMapping
    public ResponseEntity<FeaturedContent> create(@Valid @RequestBody FeaturedContentDTO dto) {
        FeaturedContent f = service.create(dto, "system");
        return ResponseEntity.status(201).body(f);
    }
}