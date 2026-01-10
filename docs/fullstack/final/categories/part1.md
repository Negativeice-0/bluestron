# Category module end-to-end (backend + frontend + verification)

We’ll implement CourseCategory end-to-end with a clean, reproducible structure:

- Package root: `co.ke.bluestron.bsapi`
- Modules: `config`, `controller`, `dto`, `entity`, `migration`, `repository`, `service`
- Application: `BsapiApplication.java`
- Frontend: Next.js page `/categories` with beauty and best practices
- Verification: curl, DB checks, and UI confirmation

This is fully commented, production-ready, and minimal enough to be teachable.

---

## Backend structure and files

```
bsapi/
  src/main/java/co/ke/bluestron/bsapi/
    BsapiApplication.java
    config/
      SecurityConfig.java
      CorsConfig.java
    controller/
      CategoryController.java
    dto/
      CategoryDTO.java
    entity/
      CourseCategory.java
    migration/
      Migration.java
      Runner.java
      V1__InitTrainingSchema.java   <-- includes course_category DDL
    repository/
      CourseCategoryRepository.java
    service/
      CategoryService.java
  src/main/resources/
    application.yaml
```

### Application entrypoint

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/BsapiApplication.java
package co.ke.bluestron.bsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Root application class. Keep this in the top-level package so component scanning
 * picks up controllers, services, repositories, etc. in subpackages.
 */
@SpringBootApplication
public class BsapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BsapiApplication.class, args);
    }
}
```

### Configuration

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/config/SecurityConfig.java
package co.ke.bluestron.bsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Minimal security for Module 1:
 * - Disable CSRF for simplicity (we'll add proper auth in Module 2).
 * - Permit public API endpoints for verification.
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/api/status", "/v3/api-docs/**", "/swagger-ui/**", "/api/categories/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/config/CorsConfig.java
package co.ke.bluestron.bsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Allow local Next.js frontend to call the API.
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

### Entity

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/entity/CourseCategory.java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * CourseCategory represents a grouping for courses (e.g., Monitoring & Evaluation).
 * Audit fields included for future admin tracking.
 */
@Entity
@Table(name = "course_category")
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String status = "active";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    private String createdBy;
    private String updatedBy;

    // Getters and setters (omitted for brevity)
    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
```

### DTO

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/dto/CategoryDTO.java
package co.ke.bluestron.bsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating categories. Validation ensures clean inputs.
 */
public record CategoryDTO(
    @NotBlank @Size(max = 100) String slug,
    @NotBlank @Size(max = 150) String name,
    @Size(max = 10000) String description,
    @NotBlank @Size(max = 20) String status
) {}
```

### Repository

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/repository/CourseCategoryRepository.java
package co.ke.bluestron.bsapi.repository;

import co.ke.bluestron.bsapi.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository with convenience lookups.
 */
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    Optional<CourseCategory> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
```

### Service

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/service/CategoryService.java
package co.ke.bluestron.bsapi.service;

import co.ke.bluestron.bsapi.dto.CategoryDTO;
import co.ke.bluestron.bsapi.entity.CourseCategory;
import co.ke.bluestron.bsapi.repository.CourseCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Business logic for categories:
 * - Prevent duplicate slugs
 * - Set audit fields
 * - Provide list/get/create/update/delete operations
 */
@Service
public class CategoryService {
    private final CourseCategoryRepository repo;

    public CategoryService(CourseCategoryRepository repo) {
        this.repo = repo;
    }

    public List<CourseCategory> list() {
        return repo.findAll();
    }

    public CourseCategory getBySlug(String slug) {
        return repo.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Transactional
    public CourseCategory create(CategoryDTO dto, String actor) {
        if (repo.existsBySlug(dto.slug())) {
            throw new IllegalArgumentException("Slug already exists");
        }
        CourseCategory c = new CourseCategory();
        c.setSlug(dto.slug());
        c.setName(dto.name());
        c.setDescription(dto.description());
        c.setStatus(dto.status());
        c.setCreatedBy(actor);
        c.setUpdatedBy(actor);
        return repo.save(c);
    }

    @Transactional
    public CourseCategory update(String slug, CategoryDTO dto, String actor) {
        CourseCategory c = getBySlug(slug);
        c.setName(dto.name());
        c.setDescription(dto.description());
        c.setStatus(dto.status());
        c.setUpdatedBy(actor);
        c.setUpdatedAt(Instant.now());
        return repo.save(c);
    }

    @Transactional
    public void delete(String slug) {
        CourseCategory c = getBySlug(slug);
        repo.delete(c);
    }
}
```

### Controller

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/controller/CategoryController.java
package co.ke.bluestron.bsapi.controller;

import co.ke.bluestron.bsapi.dto.CategoryDTO;
import co.ke.bluestron.bsapi.entity.CourseCategory;
import co.ke.bluestron.bsapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
```

### Migration (DDL)

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/migration/Migration.java
package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;

public interface Migration {
    void up(Connection conn) throws Exception;
    void down(Connection conn) throws Exception;
    String version();
    String description();
}
```

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/migration/Runner.java
package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Simple Java-based migration runner. Run via Makefile:
 * make migrate
 */
public class Runner {
    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("BSAPI_DB_URL", "jdbc:postgresql://localhost:5432/bsdb");
        String user = System.getenv().getOrDefault("BSAPI_DB_USER", "bsapi_user");
        String pass = System.getenv().getOrDefault("BSAPI_DB_PASSWORD", "CHANGE_ME_LOCAL");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            List<Migration> migrations = List.of(
                new V1__InitTrainingSchema()
            );
            for (Migration m : migrations) {
                System.out.printf("Applying %s - %s%n", m.version(), m.description());
                m.up(conn);
            }
            System.out.println("Migrations applied successfully.");
        }
    }
}
```

```java
// bsapi/src/main/java/co/ke/bluestron/bsapi/migration/V1__InitTrainingSchema.java
package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Creates course_category table with indexes and audit fields.
 */
public class V1__InitTrainingSchema implements Migration {
    @Override public String version() { return "V1"; }
    @Override public String description() { return "Init training schema (course_category)"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS course_category (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(100) UNIQUE NOT NULL,
                  name VARCHAR(150) NOT NULL,
                  description TEXT,
                  status VARCHAR(20) NOT NULL DEFAULT 'active',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_slug ON course_category(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_status ON course_category(status);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS course_category;");
        }
    }
}
```

### application.yaml

```yaml
# bsapi/src/main/resources/application.yaml
server:
  port: 8080

spring:
  application:
    name: bsapi
  datasource:
    url: jdbc:postgresql://localhost:5432/bsdb
    username: bsapi_user
    password: ${BSAPI_DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        format_sql: true
        jdbc:
          time_zone: UTC
    open-in-view: false

management:
  endpoints:
    web:
      exposure:
        include: health,mappings,info

logging:
  level:
    root: INFO
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
```

---

## Frontend (Next.js) categories page

```
bsui/
  src/app/
    categories/
      page.tsx
  src/components/
    Navbar.tsx
    Footer.tsx
```

### Navbar and Footer

```tsx
// bsui/src/components/Navbar.tsx
'use client'
import Link from 'next/link'

export default function Navbar() {
  return (
    <header className="px-4 py-3 flex items-center justify-between border-b border-navy/10 bg-softwhite">
      <div className="flex items-center gap-2">
        <div className="h-8 w-8 bg-orange rounded-md" />
        <span className="font-semibold">Bluestron</span>
      </div>
      <nav className="hidden md:flex gap-4 text-sm">
        <Link href="/" className="hover:text-orange">Home</Link>
        <Link href="/courses" className="hover:text-orange">Courses</Link>
        <Link href="/services" className="hover:text-orange">Services</Link>
        <Link href="/blog" className="hover:text-orange">Blog</Link>
        <Link href="/contact" className="hover:text-orange">Contact</Link>
      </nav>
    </header>
  )
}
```

```tsx
// bsui/src/components/Footer.tsx
export default function Footer() {
  return (
    <footer className="px-4 py-8 bg-softwhite border-t border-navy/10 text-sm">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <h4 className="font-semibold">Contact</h4>
          <p className="text-navy/70">View Park Towers, 9th Floor, Nairobi</p>
          <p className="text-navy/70">+254 702 588644</p>
          <p className="text-navy/70">info@bluestron.co.ke</p>
        </div>
        <div>
          <h4 className="font-semibold">Quick links</h4>
          <ul className="text-navy/70 space-y-1">
            <li><a href="/courses" className="hover:text-orange">Courses</a></li>
            <li><a href="/services" className="hover:text-orange">Services</a></li>
            <li><a href="/blog" className="hover:text-orange">Blog</a></li>
            <li><a href="/contact" className="hover:text-orange">Contact</a></li>
          </ul>
        </div>
        <div>
          <h4 className="font-semibold">Newsletter</h4>
          <p className="text-navy/70">Sign up to receive updates on upcoming courses.</p>
          <button className="mt-2 px-3 py-2 bg-orange text-white rounded-md">Subscribe</button>
        </div>
      </div>
      <div className="mt-6 text-navy/60">© Bluestron</div>
    </footer>
  )
}
```

### Categories page

```tsx
// bsui/src/app/categories/page.tsx
'use client'
import { useEffect, useState }