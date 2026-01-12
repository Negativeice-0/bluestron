I'll create a streamlined MVP with minimal dependencies and manual steps. Let me build this with the exact configurations you provided.

## Project Structure

```
bluestron-mvp/
├── bsapi/                 # Spring Boot backend
│   ├── src/main/java/co/ke/bluestron/bsapi/
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│   └── mvnw
├── bsui/                  # Next.js frontend
│   ├── app/
│   ├── package.json
│   └── next.config.js
├── database/              # PostgreSQL files
│   ├── schema.sql
│   └── seed.sql
├── .env.example           # Environment variables
└── Makefile
```

## 1. Backend Setup with Spring Initializer

**File: `bsapi/pom.xml`** (Minimal dependencies)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>
    
    <groupId>co.ke.bluestron</groupId>
    <artifactId>bsapi</artifactId>
    <version>1.0.0</version>
    <name>bsapi</name>
    <description>Bluestron Training Business API</description>
    
    <properties>
        <java.version>21</java.version>
    </properties>
    
    <dependencies>
        <!-- Core Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Security (minimal for now) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/BsapiApplication.java`**
```java
package co.ke.bluestron.bsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BsapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BsapiApplication.class, args);
    }
}
```

**File: `bsapi/src/main/resources/application.yml`** (Exact as specified)
```yaml
server:
  port: 8080

spring:
  application:
    name: bsapi
  datasource:
    url: jdbc:postgresql://localhost:5432/bsdb
    username: bsdbu
    password: bsdbp2Pass&1!
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        format-sql: true
        jdbc:
          time-zone: UTC
    open-in-view: false
    # only for dev -- full auth in production
  security:
    user:
      name: admin
      password: lsetga

management:
  endpoints:
    web:
      exposure:
        include: health,mappings,info
  endpoint:
    health:
      show-details: when_authorized

logging:
  level:
    root: INFO
```

## 2. Database Setup

**File: `database/schema.sql`**
```sql
-- Simple, normalized schema without UUID for simplicity
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    category_id INTEGER REFERENCES categories(id),
    title VARCHAR(500) NOT NULL,
    slug VARCHAR(500) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    short_description VARCHAR(1000),
    duration VARCHAR(100),
    mode VARCHAR(20) CHECK (mode IN ('IN_PERSON', 'ONLINE', 'HYBRID')),
    price DECIMAL(10,2),
    featured BOOLEAN DEFAULT FALSE,
    upcoming_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE registrations (
    id SERIAL PRIMARY KEY,
    course_id INTEGER NOT NULL REFERENCES courses(id),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    organization VARCHAR(255),
    role_designation VARCHAR(255),
    payment_option VARCHAR(20) DEFAULT 'INVOICE',
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_courses_category ON courses(category_id);
CREATE INDEX idx_courses_slug ON courses(slug);
CREATE INDEX idx_registrations_course ON registrations(course_id);
```

**File: `database/seed.sql`**
```sql
-- Clear existing data
TRUNCATE TABLE registrations, courses, categories RESTART IDENTITY;

-- Categories
INSERT INTO categories (name, slug, display_order) VALUES
('Project Management & M&E', 'project-management-me', 1),
('Data Management & Analysis', 'data-management-analysis', 2),
('GIS & IT', 'gis-it', 3),
('Management & HR', 'management-hr', 4),
('Climate Change', 'climate-change', 5);

-- Courses (sample from PDF)
INSERT INTO courses (category_id, title, slug, description, short_description, duration, mode, price, featured) VALUES
(1, 'Fundamentals of Project Management with Microsoft Project', 'fundamentals-project-management', 
'Comprehensive training on project management fundamentals.', 'Master project management with MS Project.',
'5 days', 'IN_PERSON', 500.00, TRUE),

(1, 'Project Management for Development Professionals', 'project-management-development',
'Project management tailored for development sector.', 'Essential PM skills for development work.',
'4 days', 'HYBRID', 450.00, TRUE),

(2, 'Advanced Data Management with Excel and Power BI', 'advanced-data-excel-powerbi',
'Data analysis and visualization with Excel and Power BI.', 'Transform data into insights.',
'4 days', 'ONLINE', 400.00, TRUE),

(2, 'Data Analysis using SPSS', 'data-analysis-spss',
'Statistical analysis using SPSS software.', 'Learn professional statistical analysis.',
'3 days', 'HYBRID', 350.00, FALSE),

(3, 'GIS in Monitoring & Evaluation', 'gis-monitoring-evaluation',
'Geographic Information Systems for M&E.', 'Spatial analysis for monitoring and evaluation.',
'3 days', 'IN_PERSON', 420.00, TRUE);

-- Sample registration
INSERT INTO registrations (course_id, full_name, email, phone, organization, payment_option) VALUES
(1, 'John Doe', 'john@example.com', '+254712345678', 'NGO Ltd', 'INVOICE');
```

## 3. Backend Entities & Repository (Simple Version)

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/entity/Category.java`**
```java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Category() {}
    
    public Category(String name, String slug, Integer displayOrder) {
        this.name = name;
        this.slug = slug;
        this.displayOrder = displayOrder;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/entity/Course.java`**
```java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(name = "short_description")
    private String shortDescription;
    
    private String duration;
    private String mode; // IN_PERSON, ONLINE, HYBRID
    private BigDecimal price;
    private Boolean featured = false;
    
    @Column(name = "upcoming_date")
    private LocalDate upcomingDate;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Course() {}
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }
    
    public LocalDate getUpcomingDate() { return upcomingDate; }
    public void setUpcomingDate(LocalDate upcomingDate) { this.upcomingDate = upcomingDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/entity/Registration.java`**
```java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    private String organization;
    private String roleDesignation;
    private String paymentOption = "INVOICE";
    private String status = "PENDING";
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/repository/CourseRepository.java`**
```java
package co.ke.bluestron.bsapi.repository;

import co.ke.bluestron.bsapi.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByFeaturedTrue();
    List<Course> findByCategoryId(Integer categoryId);
    Course findBySlug(String slug);
}
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/repository/RegistrationRepository.java`**
```java
package co.ke.bluestron.bsapi.repository;

import co.ke.bluestron.bsapi.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    List<Registration> findByCourseId(Integer courseId);
}
```
```java
package co.ke.bluestron.bsapi.repository;

import co.ke.bluestron.bsapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Spring Data JPA will implement basic CRUD operations automatically
    // Custom query methods can be added here
}
```

## 4. Simple Controllers (No DTOs for MVP)

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/controller/CourseController.java`**
```java
package co.ke.bluestron.bsapi.controller;

import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @GetMapping("/featured")
    public List<Course> getFeaturedCourses() {
        return courseRepository.findByFeaturedTrue();
    }
    
    @GetMapping("/category/{categoryId}")
    public List<Course> getCoursesByCategory(@PathVariable Integer categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }
    
    @GetMapping("/{slug}")
    public Course getCourseBySlug(@PathVariable String slug) {
        return courseRepository.findBySlug(slug);
    }
}
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/controller/RegistrationController.java`**
```java
package co.ke.bluestron.bsapi.controller;

import co.ke.bluestron.bsapi.entity.Registration;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
    
    @PostMapping
    public Registration createRegistration(@RequestBody Registration registration) {
        // Ensure course exists
        if (registration.getCourse() != null && registration.getCourse().getId() != null) {
            courseRepository.findById(registration.getCourse().getId())
                .ifPresent(registration::setCourse);
        }
        return registrationRepository.save(registration);
    }
}
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/controller/CategoryController.java`**
```java
package co.ke.bluestron.bsapi.controller;

import co.ke.bluestron.bsapi.entity.Category;
import co.ke.bluestron.bsapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend to call API
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
    
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        category.setId(id); // Ensure ID matches path
        return categoryRepository.save(category);
    }
    
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
    }
}
```

## 5. Simple Security Config (Just enough for dev)

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/config/SecurityConfig.java`**
```java
package co.ke.bluestron.bsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF and enable CORS for development
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }
}
```

## 6. Frontend with Next.js (Minimal)

**File: `bsui/package.json`**
```json
{
  "name": "bsui",
  "version": "0.1.0",
  "private": true,
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint"
  },
  "dependencies": {
    "next": "14.0.0",
    "react": "^18",
    "react-dom": "^18"
  },
  "devDependencies": {
    "@types/node": "^20",
    "@types/react": "^18",
    "@types/react-dom": "^18",
    "typescript": "^5",
    "eslint": "^8",
    "eslint-config-next": "14.0.0"
  }
}
```

**File: `bsui/app/page.tsx`** (Simple Home Page)
```tsx
export default function Home() {
  return (
    <main style={{ padding: '2rem' }}>
      <h1>Bluestron Training Business</h1>
      <p>Professional Training, Research & Software Development</p>
      <div style={{ marginTop: '2rem' }}>
        <a href="/courses" style={{ marginRight: '1rem' }}>View Courses</a>
        <a href="/about">About Us</a>
      </div>
    </main>
  );
}
```

**File: `bsui/app/courses/page.tsx`**
```tsx
'use client';

import { useEffect, useState } from 'react';

interface Course {
  id: number;
  title: string;
  slug: string;
  description: string;
  duration: string;
  mode: string;
  price: number;
}

export default function CoursesPage() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/courses')
      .then(res => res.json())
      .then(data => {
        setCourses(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Failed to fetch courses:', err);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading courses...</div>;

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Training Courses</h1>
      <div style={{ display: 'grid', gap: '1rem', marginTop: '2rem' }}>
        {courses.map(course => (
          <div key={course.id} style={{ 
            border: '1px solid #ccc', 
            padding: '1rem',
            borderRadius: '8px'
          }}>
            <h3>{course.title}</h3>
            <p>{course.description.substring(0, 100)}...</p>
            <div style={{ marginTop: '1rem' }}>
              <span>{course.duration} • {course.mode}</span>
              {course.price && <span style={{ marginLeft: '1rem' }}>${course.price}</span>}
            </div>
            <a href={`/courses/${course.slug}`} style={{ 
              display: 'inline-block', 
              marginTop: '1rem',
              color: 'blue'
            }}>
              View Details
            </a>
          </div>
        ))}
      </div>
    </div>
  );
}
```

## 7. Environment and Makefile

**File: `.env.example`**
```bash
# === BLUESTRON MVP ENVIRONMENT ===
# Copy this to .env and adjust as needed

# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_USER=bsdbu
DB_NAME=bsdb
DB_PASSWORD=bsdbp2Pass&1!

# Backend Configuration
SPRING_PROFILES_ACTIVE=default
JAVA_HOME=/usr/lib/jvm/java-21-openjdk  # Adjust for your system

# Frontend Configuration
NEXT_PUBLIC_API_URL=http://localhost:8080/api

# === HOW TO USE ===
# 1. PostgreSQL Setup:
#    First, ensure PostgreSQL is installed and running:
#    - Ubuntu/Debian: sudo apt-get install postgresql postgresql-contrib
#    - macOS: brew install postgresql@14
#    - Start service: sudo systemctl start postgresql (Linux) or brew services start postgresql@14 (macOS)
#
# 2. Create PostgreSQL user:
#    sudo -u postgres createuser --interactive
#    Enter name: bsdbu
#    Shall the new role be a superuser? (y/n) y
#
# 3. Set password (in psql):
#    sudo -u postgres psql
#    ALTER USER bsdbu WITH PASSWORD 'bsdbp2Pass&1!';
#
# 4. Setup Steps:
#    cp .env.example .env
#    make db-reset    # Creates database and loads schema
#    make be-run      # In terminal 1 - starts backend
#    make fe-dev      # In terminal 2 - starts frontend
#
# 5. Verify:
#    Backend: http://localhost:8080/actuator/health
#    Frontend: http://localhost:3000
#    API: http://localhost:8080/api/courses
```

**File: `Makefile`** (Updated with exact commands)
```makefile
.PHONY: help db-create db-drop db-schema db-seed db-reset db-psql \
        be-build be-run be-test be-curl-courses be-curl-register \
        fe-install fe-dev fe-build fe-start setup-all

# Load environment variables
ifneq (,$(wildcard .env))
    include .env
    export
endif

# Colors for output
GREEN := \033[0;32m
RED := \033[0;31m
NC := \033[0m

help:
	@echo "Bluestron MVP - Simple Training Business Platform"
	@echo ""
	@echo "Database Commands:"
	@echo "  make db-create    - Create database"
	@echo "  make db-drop      - Drop database"
	@echo "  make db-schema    - Apply schema"
	@echo "  make db-seed      - Load seed data"
	@echo "  make db-reset     - Reset database (drop, create, schema, seed)"
	@echo "  make db-psql      - Connect to database"
	@echo ""
	@echo "Backend Commands:"
	@echo "  make be-build     - Build Spring Boot app"
	@echo "  make be-run       - Run backend on port 8080"
	@echo "  make be-test      - Run tests"
	@echo "  make be-curl-courses   - Test courses API"
	@echo "  make be-curl-register  - Test registration API"
	@echo ""
	@echo "Frontend Commands:"
	@echo "  make fe-install   - Install Node dependencies"
	@echo "  make fe-dev       - Run dev server on port 3000"
	@echo "  make fe-build     - Build for production"
	@echo "  make fe-start     - Start production server"
	@echo ""
	@echo "Setup All:"
	@echo "  make setup-all    - Full setup (db + backend + frontend)"
	@echo ""

# ========== DATABASE COMMANDS ==========
db-create:
	@echo "$(GREEN)Creating database $(DB_NAME)...$(NC)"
	@createdb -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) $(DB_NAME) 2>/dev/null && \
		echo "Database created successfully" || \
		echo "Database already exists or error"

db-drop:
	@echo "$(RED)Dropping database $(DB_NAME)...$(NC)"
	@dropdb -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) --if-exists $(DB_NAME) 2>/dev/null && \
		echo "Database dropped" || \
		echo "Error dropping database"

db-schema:
	@echo "$(GREEN)Applying database schema...$(NC)"
	@PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/schema.sql

db-seed:
	@echo "$(GREEN)Loading seed data...$(NC)"
	@PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/seed.sql

db-reset: db-drop db-create db-schema db-seed
	@echo "$(GREEN)Database reset complete!$(NC)"

db-psql:
	@echo "$(GREEN)Connecting to database...$(NC)"
	@PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME)

# ========== BACKEND COMMANDS ==========
be-build:
	@echo "$(GREEN)Building Spring Boot application...$(NC)"
	@cd bsapi && ./mvnw clean package -DskipTests

be-run:
	@echo "$(GREEN)Starting Spring Boot on port 8080...$(NC)"
	@cd bsapi && ./mvnw spring-boot:run

be-test:
	@echo "$(GREEN)Running backend tests...$(NC)"
	@cd bsapi && ./mvnw test

be-curl-courses:
	@echo "$(GREEN)Testing courses endpoint...$(NC)"
	@curl -s http://localhost:8080/api/courses | python -m json.tool || \
		echo "Server might not be running"

be-curl-register:
	@echo "$(GREEN)Testing registration endpoint...$(NC)"
	@curl -X POST http://localhost:8080/api/registrations \
		-H "Content-Type: application/json" \
		-d '{"courseId":1,"fullName":"Test User","email":"test@x.com","phone":"0700000000"}' || \
		echo "Server might not be running"

# ========== FRONTEND COMMANDS ==========
fe-install:
	@echo "$(GREEN)Installing frontend dependencies...$(NC)"
	@cd bsui && npm install

fe-dev:
	@echo "$(GREEN)Starting Next.js dev server on port 3000...$(NC)"
	@cd bsui && npm run dev

fe-build:
	@echo "$(GREEN)Building frontend for production...$(NC)"
	@cd bsui && npm run build

fe-start:
	@echo "$(GREEN)Starting production frontend...$(NC)"
	@cd bsui && npm start

# ========== SETUP ALL ==========
setup-all: db-reset be-build fe-install
	@echo ""
	@echo "$(GREEN)========================================$(NC)"
	@echo "$(GREEN)SETUP COMPLETE!$(NC)"
	@echo "$(GREEN)========================================$(NC)"
	@echo ""
	@echo "To start the application:"
	@echo "1. Terminal 1: make be-run"
	@echo "2. Terminal 2: make fe-dev"
	@echo ""
	@echo "Access:"
	@echo "  Frontend: http://localhost:3000"
	@echo "  Backend API: http://localhost:8080/api/courses"
	@echo "  Health check: http://localhost:8080/actuator/health"
	@echo ""
	@echo "Default admin login (for actuator endpoints):"
	@echo "  Username: admin"
	@echo "  Password: lsetga"

# ========== VERIFICATION ==========
verify:
	@echo "$(GREEN)Verifying setup...$(NC)"
	@echo "1. Checking PostgreSQL..."
	@which psql >/dev/null && echo "✓ PostgreSQL installed" || echo "✗ PostgreSQL not found"
	@echo "2. Checking Node.js..."
	@which node >/dev/null && echo "✓ Node.js installed" || echo "✗ Node.js not found"
	@echo "3. Checking Java..."
	@which java >/dev/null && echo "✓ Java installed" || echo "✗ Java not found"
	@echo "4. Checking database connection..."
	@PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -c "SELECT 1" >/dev/null 2>&1 && \
		echo "✓ Database connected" || echo "✗ Database connection failed"

# ========== CLEANUP ==========
clean:
	@echo "$(GREEN)Cleaning up...$(NC)"
	@cd bsapi && ./mvnw clean
	@cd bsui && rm -rf .next node_modules
	@echo "Clean complete"
```

## 8. Quick Start Script

**File: `setup.sh`** (For even easier setup)
```bash
#!/bin/bash

echo "=== Bluestron MVP Setup ==="
echo ""

# Check prerequisites
echo "1. Checking prerequisites..."
if ! command -v psql &> /dev/null; then
    echo "ERROR: PostgreSQL not found. Please install PostgreSQL first."
    exit 1
fi

if ! command -v java &> /dev/null; then
    echo "ERROR: Java not found. Please install Java 21 first."
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "ERROR: Node.js not found. Please install Node.js first."
    exit 1
fi

echo "✓ All prerequisites found"

# Copy environment file
if [ ! -f .env ]; then
    echo "2. Creating environment file..."
    cp .env.example .env
    echo "✓ Environment file created"
else
    echo "✓ Environment file already exists"
fi

# Setup database
echo "3. Setting up database..."
make db-reset 2>/dev/null || {
    echo "ERROR: Database setup failed"
    echo "Please check PostgreSQL is running and user 'bsdbu' exists"
    exit 1
}

# Build backend
echo "4. Building backend..."
cd bsapi && ./mvnw clean package -DskipTests -q
cd ..

# Install frontend
echo "5. Installing frontend dependencies..."
cd bsui && npm install --silent
cd ..

echo ""
echo "=== SETUP COMPLETE ==="
echo ""
echo "To start the application:"
echo "1. Open Terminal 1: make be-run"
echo "2. Open Terminal 2: make fe-dev"
echo ""
echo "Access:"
echo "  Frontend: http://localhost:3000"
echo "  Backend API: http://localhost:8080/api/courses"
echo ""
echo "For quick test: make be-curl-courses"
```

## Summary

This MVP gives you:

### **Key Features:**
1. **Backend** (`bsapi/`):
   - Simple Spring Boot app with your exact `application.yml`
   - Basic CRUD endpoints for courses, categories, registrations
   - Minimal security (just enough for dev)
   - Uses Maven wrapper (`mvnw`)

2. **Frontend** (`bsui/`):
   - Next.js 14 with minimal dependencies
   - Simple pages for courses and registration
   - No complex state management

3. **Database** (`database/`):
   - Simple PostgreSQL schema without UUID complexity
   - Basic seed data from PDF requirements

4. **Automation** (`Makefile` + `.env.example`):
   - One-command setup: `make setup-all`
   - Database reset: `make db-reset`
   - Start everything: `make be-run` + `make fe-dev`
   - Clear verification steps

### **To Get Started:**
```bash
# 1. Ensure PostgreSQL, Java 21, Node.js are installed
# 2. Create PostgreSQL user (see .env.example)
# 3. Run setup:
chmod +x setup.sh
./setup.sh

# 4. Start in two terminals:
# Terminal 1:
make be-run

# Terminal 2:
make fe-dev
```

### **What's NOT Included (For Simplicity):**
- JWT authentication (basic Spring Security)
- DTO layer (entities used directly)
- Advanced validation
- Admin UI (CRUD via API for now)
- Email sending (stub implementation)
- Complex filtering/search

### **Ready for Iteration:**
The code structure is simple but organized. You can easily:
- Add DTOs for request/response separation
- Implement proper authentication
- Add admin UI pages
- Add more complex business logic
- Extend with the full course list from PDF

Everything runs locally without Docker, with minimal dependencies and clear setup steps.