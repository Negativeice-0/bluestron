Excellent — now that **Categories** are working end‑to‑end, let’s scaffold **Courses** with the same rigor: backend entity → DTO → repository → service → controller → migration → curl verification → frontend UI page. This will give you confidence that courses can be created, listed, and consumed through the full stack.

---

## Backend structure for Course

```
bsapi/
  src/main/java/co/ke/bluestron/bsapi/
    entity/Course.java
    dto/CourseDTO.java
    repository/CourseRepository.java
    service/CourseService.java
    controller/CourseController.java
    migration/V2__InitCourseSchema.java
```

### Entity

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/entity/Course.java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * Course entity belongs to a CourseCategory.
 * Includes audit fields for admin tracking.
 */
@Entity
@Table(name = "course")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @ElementCollection
    @CollectionTable(name = "course_outcomes", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "outcome")
    private List<String> learningOutcomes;

    private String thumbnailUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @Column(nullable = false, length = 20)
    private String status = "draft";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    private String createdBy;
    private String updatedBy;

    // getters/setters omitted for brevity
}
```

### DTO

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/dto/CourseDTO.java
package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO for creating/updating courses.
 */
public record CourseDTO(
    @NotBlank @Size(max = 150) String slug,
    @NotBlank @Size(max = 200) String title,
    String shortDescription,
    String fullDescription,
    List<@NotBlank String> learningOutcomes,
    String thumbnailUrl,
    @NotNull Long categoryId,
    @Pattern(regexp = "draft|published|archived") String status
) {}
```

### Repository

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/repository/CourseRepository.java
package co.ke.bluestron.bsapi.repository;

import co.ke.bluestron.bsapi.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findBySlug(String slug);

    @Query("SELECT c FROM Course c WHERE (:categoryId IS NULL OR c.category.id = :categoryId) AND (:status IS NULL OR c.status = :status)")
    List<Course> findByFilters(Long categoryId, String status);
}
```

### Service

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/service/CourseService.java
package co.ke.bluestron.bsapi.service;

import co.ke.bluestron.bsapi.dto.CourseDTO;
import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.entity.CourseCategory;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.CourseCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepo;
    private final CourseCategoryRepository categoryRepo;

    public CourseService(CourseRepository courseRepo, CourseCategoryRepository categoryRepo) {
        this.courseRepo = courseRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Course> list(Long categoryId, String status) {
        return courseRepo.findByFilters(categoryId, status);
    }

    public Course getBySlug(String slug) {
        return courseRepo.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    @Transactional
    public Course create(CourseDTO dto, String actor) {
        CourseCategory cat = categoryRepo.findById(dto.categoryId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Course c = new Course();
        c.setSlug(dto.slug());
        c.setTitle(dto.title());
        c.setShortDescription(dto.shortDescription());
        c.setFullDescription(dto.fullDescription());
        c.setLearningOutcomes(dto.learningOutcomes());
        c.setThumbnailUrl(dto.thumbnailUrl());
        c.setCategory(cat);
        c.setStatus(dto.status() == null ? "draft" : dto.status());
        c.setCreatedBy(actor);
        c.setUpdatedBy(actor);
        return courseRepo.save(c);
    }
}
```

### Controller

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/controller/CourseController.java
package co.ke.bluestron.bsapi.controller;

import co.ke.bluestron.bsapi.dto.CourseDTO;
import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
```

### Migration

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/migration/V2__InitCourseSchema.java
package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

public class V2__InitCourseSchema implements Migration {
    @Override public String version() { return "V2"; }
    @Override public String description() { return "Init course schema"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS course (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(150) UNIQUE NOT NULL,
                  title VARCHAR(200) NOT NULL,
                  short_description TEXT,
                  full_description TEXT,
                  thumbnail_url TEXT,
                  category_id INT NOT NULL REFERENCES course_category(id) ON DELETE RESTRICT,
                  status VARCHAR(20) NOT NULL DEFAULT 'draft',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_slug ON course(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_id ON course(category_id);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS course;");
        }
    }
}
```

---

## Curl verification

- **Create course**
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "advanced-me-health-programs",
    "title": "Advanced M&E and Data Management for Health Programs",
    "shortDescription": "Deep-dive into M&E for health.",
    "fullDescription": "Full syllabus...",
    "learningOutcomes": ["Design M&E frameworks", "Analyze health data"],
    "thumbnailUrl": "https://example.com/img/health-me.png",
    "categoryId": 1,
    "status": "published"
  }'
```

- **List courses**
```bash
curl "http://localhost:8080/api/courses?categoryId=1&status=published"
```

- **Get course by slug**
```bash
curl http://localhost:8080/api/courses/advanced-me-health-programs
```

---

## Frontend: Courses page

```tsx
// bsui/src/app/courses/page.tsx
'use client'
import { useEffect,