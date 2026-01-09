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

import co.ke.bluestron.bsapi.dto.BlogPostDTO;
import co.ke.bluestron.bsapi.entity.BlogPost;
import co.ke.bluestron.bsapi.service.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Blog Posts")
@RestController @RequestMapping("/api/blog-posts")
public class BlogPostController {
    private final BlogPostService service;
    public BlogPostController(BlogPostService service) { this.service = service; }

    @Operation(summary = "List blog posts with optional filter")
    @GetMapping
    public ResponseEntity<List<BlogPost>> list(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(status));
    }

    @Operation(summary = "Create a blog post")
    @PostMapping
    public ResponseEntity<BlogPost> create(@Valid @RequestBody BlogPostDTO dto) {
        BlogPost b = service.create(dto, "system");
        return ResponseEntity.status(201).body(b);
    }

    @Operation(summary = "Get blog post by slug")
    @GetMapping("/{slug}")
    public ResponseEntity<BlogPost> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }
}