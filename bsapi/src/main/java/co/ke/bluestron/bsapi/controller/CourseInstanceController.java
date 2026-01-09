package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.dto.CourseInstanceDTO;
import co.ke.bluestron.bsapi.entity.CourseInstance;
import co.ke.bluestron.bsapi.service.CourseInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Course Instances")
@RestController @RequestMapping("/api/course-instances")
public class CourseInstanceController {
    private final CourseInstanceService service;
    public CourseInstanceController(CourseInstanceService service) { this.service = service; }

    @Operation(summary = "List course instances with optional filters")
    @GetMapping
    public ResponseEntity<List<CourseInstance>> list(@RequestParam(required = false) Long courseId,
                                                     @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(courseId, status));
    }

    @Operation(summary = "Create a course instance")
    @PostMapping
    public ResponseEntity<CourseInstance> create(@Valid @RequestBody CourseInstanceDTO dto) {
        CourseInstance ci = service.create(dto, "system");
        return ResponseEntity.status(201).body(ci);
    }
}