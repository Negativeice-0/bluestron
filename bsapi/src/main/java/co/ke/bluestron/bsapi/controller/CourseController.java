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

import co.ke.bluestron.bsapi.dto.CourseDTO;
import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Courses")
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @Operation(summary = "List courses with optional filters")
    @GetMapping
    public ResponseEntity<List<Course>> list(@RequestParam(required = false) Long categoryId,
                                             @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(categoryId, status));
    }

    @Operation(summary = "Create a course")
    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody CourseDTO dto) {
        Course c = service.create(dto, "system");
        return ResponseEntity.status(201).body(c);
    }

    @Operation(summary = "Get course by slug")
    @GetMapping("/{slug}")
    public ResponseEntity<Course> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }
}
