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

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Course entity belongs to a CourseCategory.
 * Includes audit fields for admin tracking.
 * 
 * ANALOGY: Think of getters as the backend equivalent of frontend READ/DISPLAY operations,
 * and setters as the backend equivalent of frontend EDIT operations.
 * Just like a frontend has edit/delete buttons to modify data,
 * the backend provides setters to modify entity fields and getters to read them.
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
    //When serializing a CourseCategory from within a Course, ignore the courses field
    @JsonIgnoreProperties({"courses"}) // prevent infinite recursion
    private CourseCategory category;

    @Column(nullable = false, length = 20)
    private String status = "draft";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    private String createdBy;
    private String updatedBy;

    // ============ GETTERS (Backend READ operations) ============
    // These are like the frontend's display/read operations - they retrieve data for viewing
    
    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public List<String> getLearningOutcomes() {
        return learningOutcomes;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    // ============ SETTERS (Backend EDIT operations) ============
    // These are like the frontend's edit buttons - they modify the entity data
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setLearningOutcomes(List<String> learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
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

# !!Update Runner.java:

java
List<Migration> migrations = List.of(
    new V1__InitTrainingSchema(),
    new V2__InitCourseSchema()
);

# !Run:

bash
make api-build

then

make migrate

# !Check DB:

# !bash
psql -U bsapi_user -d bsdb -c "\dt"
→ You should now see both course_category and course.

---