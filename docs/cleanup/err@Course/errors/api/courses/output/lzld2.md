That error means your controller is still returning the JPA entity directly, so Jackson is trying to serialize the lazy learningOutcomes collection after the Hibernate session has closed. Even though you created a DTO, if you don’t actually return the DTO from the controller, the entity leaks out and triggers the lazy‑init error.

✅ How to fix cleanly
1. Make sure your entity is defined with lazy outcomes
java
@ElementCollection(fetch = FetchType.LAZY)
@CollectionTable(name = "course_outcomes", joinColumns = @JoinColumn(name = "course_id"))
@Column(name = "outcome")
private List<String> learningOutcomes;
2. Define a response DTO
java
public record CourseResponse(
    Long id,
    String slug,
    String title,
    String shortDescription,
    String fullDescription,
    List<String> learningOutcomes,
    String thumbnailUrl,
    String status,
    CourseCategory category
) {}
3. Map entity → DTO in the controller


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
import co.ke.bluestron.bsapi.dto.CourseResponse;
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
    public ResponseEntity<List<CourseResponse>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status) {
        
        List<CourseResponse> dtos = service.list(categoryId, status).stream()
            .map(this::convertToResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get course by slug")
    @GetMapping("/{slug}")
    public ResponseEntity<CourseResponse> get(@PathVariable String slug) {
        var course = service.getBySlug(slug);
        return ResponseEntity.ok(convertToResponse(course));
    }

    @Operation(summary = "Create a course")
    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseDTO dto) {
        var course = service.create(dto, "system");
        return ResponseEntity.status(201).body(convertToResponse(course));
    }

    /**
     * Helper method to convert Course entity to CourseResponse DTO
     * Ensures consistent mapping and defensive copying
     */
    private CourseResponse convertToResponse(co.ke.bluestron.bsapi.entity.Course course) {
        return new CourseResponse(
            course.getId(),
            course.getSlug(),
            course.getTitle(),
            course.getShortDescription(),
            course.getFullDescription(),
            // Defensive copy for mutable collections
            course.getLearningOutcomes() != null ? List.copyOf(course.getLearningOutcomes()) : List.of(),
            course.getThumbnailUrl(),
            course.getStatus(),
            course.getCategory()
        );
    }
}


👉 Notice: we never return Course directly. We always build a CourseResponse DTO. That forces Hibernate to initialize the collection inside the service method, while the session is still open.

4. Alternative quick fix
If you want to unblock testing without DTOs, you can mark the collection as eager:

java
@ElementCollection(fetch = FetchType.EAGER)
private List<String> learningOutcomes;
But this is less efficient and not recommended long‑term.

🔎 Next step
Update your controller to return DTOs as above.

Retry:

bash
curl http://localhost:8080/api/courses/fundamentals-project-management
You should now get a proper JSON response with learningOutcomes: [] or the values you inserted.

Would you like me to also show you how to update the service layer so it explicitly initializes learningOutcomes before mapping to DTO, ensuring no lazy proxy issues even if you keep FetchType.LAZY?