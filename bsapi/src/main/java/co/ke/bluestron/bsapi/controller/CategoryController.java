package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.CategoryDTO;
import co.ke.bluestron.bsapi.entity.CourseCategory;
import co.ke.bluestron.bsapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Public endpoints for categories (Module 1).
 * In Module 2, we'll add admin-protected endpoints under /api/admin/categories.
 */
@Tag(name = "Categories")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "List categories")
    @GetMapping
    public ResponseEntity<List<CourseCategory>> list() {
        return ResponseEntity.ok(service.list());
    }

    @Operation(summary = "Create a category")
    @PostMapping
    public ResponseEntity<CourseCategory> create(@Valid @RequestBody CategoryDTO dto) {
        CourseCategory c = service.create(dto, "system"); // actor will be authenticated user in Module 2
        return ResponseEntity.status(201).body(c);
    }

    @Operation(summary = "Get category by slug")
    @GetMapping("/{slug}")
    public ResponseEntity<CourseCategory> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }

    @Operation(summary = "Update category by slug")
    @PutMapping("/{slug}")
    public ResponseEntity<CourseCategory> update(@PathVariable String slug, @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.update(slug, dto, "system"));
    }

    @Operation(summary = "Delete category by slug")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        service.delete(slug);
        return ResponseEntity.noContent().build();
    }
}
