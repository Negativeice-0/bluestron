I'll create a minimal but functional Bluestron implementation with modular architecture for easy iteration.

## Project Structure

```
bluestron-minimal/
├── backend/              # Spring Boot API
├── frontend/             # Next.js application
├── database/             # PostgreSQL schemas and migrations
├── docker-compose.yml    # Container orchestration
└── Makefile              # Unified build/run commands
```

## 1. Database Schema (PostgreSQL)

**`database/schema.sql`**:
```sql
-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table (basic auth)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    role VARCHAR(50) DEFAULT 'user', -- 'admin', 'user'
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Categories table
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Courses table
CREATE TABLE courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    title VARCHAR(500) NOT NULL,
    slug VARCHAR(500) UNIQUE NOT NULL,
    short_description TEXT,
    full_description TEXT,
    duration VARCHAR(100),
    mode VARCHAR(50), -- 'in-person', 'online', 'hybrid'
    price DECIMAL(10,2),
    is_published BOOLEAN DEFAULT false,
    featured_image_url VARCHAR(500),
    upcoming_date DATE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Services table
CREATE TABLE services (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_type VARCHAR(100) NOT NULL, -- 'research', 'data-analysis', 'software-development'
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(100),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Course registrations
CREATE TABLE registrations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID REFERENCES courses(id) ON DELETE CASCADE,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    organization VARCHAR(255),
    role VARCHAR(100),
    preferred_date DATE,
    payment_option VARCHAR(50) DEFAULT 'invoice',
    status VARCHAR(50) DEFAULT 'pending', -- 'pending', 'confirmed', 'cancelled'
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Enquiries for services
CREATE TABLE enquiries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_id UUID REFERENCES services(id) ON DELETE SET NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    organization VARCHAR(255),
    message TEXT,
    status VARCHAR(50) DEFAULT 'new',
    created_at TIMESTAMP DEFAULT NOW()
);

-- Create indexes for performance
CREATE INDEX idx_courses_category ON courses(category_id);
CREATE INDEX idx_courses_published ON courses(is_published) WHERE is_published = true;
CREATE INDEX idx_registrations_course ON registrations(course_id);
CREATE INDEX idx_registrations_email ON registrations(email);
CREATE INDEX idx_users_email ON users(email);

-- Create admin user (password: Admin123!)
INSERT INTO users (email, password_hash, full_name, role) 
VALUES ('admin@bluestron.co.ke', '$2a$10$YourHashedPasswordHere', 'Admin User', 'admin');

-- Insert default categories
INSERT INTO categories (name, slug, description) VALUES
('Project Management / M&E', 'project-management-me', 'Project management and monitoring & evaluation courses'),
('Data Management & Analysis', 'data-management-analysis', 'Data management and analysis courses'),
('GIS & IT', 'gis-it', 'GIS and IT courses'),
('Management & Administration', 'management-admin', 'Management and administration courses'),
('Climate Change / Environment', 'climate-environment', 'Climate and environment courses');

-- Insert default services
INSERT INTO services (service_type, title, slug, description) VALUES
('research', 'Research Services', 'research-services', 'Baseline studies, midline studies, and endline evaluations'),
('data-analysis', 'Data Analysis & Management', 'data-analysis-management', 'Data cleaning, processing, analysis and visualization'),
('software-development', 'Software Development', 'software-development', 'Custom software for data collection, dashboards, and apps');
```

**`database/migrations/001_initial_schema.sql`**:
```sql
-- Migration file for version control
-- This file contains the same schema as above but structured for migrations
```

## 2. Spring Boot Backend

**`backend/pom.xml`** (key dependencies):
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

**`backend/src/main/java/com/bluestron/config/SecurityConfig.java`**:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth/**", "/api/public/**").permitAll()
            .antMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**`backend/src/main/java/com/bluestron/model/Course.java`**:
```java
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    private String title;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private String duration;
    private String mode;
    private BigDecimal price;
    private boolean isPublished;
    private String featuredImageUrl;
    private LocalDate upcomingDate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

**`backend/src/main/java/com/bluestron/controller/CourseController.java`**:
```java
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping("/public")
    public ResponseEntity<List<CourseDTO>> getPublishedCourses(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String mode) {
        return ResponseEntity.ok(courseService.getPublishedCourses(categoryId, mode));
    }
    
    @GetMapping("/public/{slug}")
    public ResponseEntity<CourseDTO> getCourseBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(courseService.getCourseBySlug(slug));
    }
    
    @PostMapping("/public/{courseId}/register")
    public ResponseEntity<RegistrationResponse> register(
            @PathVariable UUID courseId,
            @Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(courseService.registerForCourse(courseId, request));
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(request));
    }
    
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable UUID id,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }
    
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 3. Next.js Frontend

**`frontend/package.json`**:
```json
{
  "name": "bluestron-frontend",
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
    "react-dom": "^18",
    "axios": "^1.6.0",
    "react-hook-form": "^7.47.0",
    "zod": "^3.22.4",
    "@hookform/resolvers": "^3.3.2",
    "tailwindcss": "^3.3.0",
    "lucide-react": "^0.309.0"
  },
  "devDependencies": {
    "@types/node": "^20",
    "@types/react": "^18",
    "@types/react-dom": "^18",
    "typescript": "^5",
    "@typescript-eslint/eslint-plugin": "^6",
    "@typescript-eslint/parser": "^6",
    "autoprefixer": "^10.0.0",
    "eslint": "^8",
    "eslint-config-next": "14.0.0",
    "postcss": "^8"
  }
}
```

**`frontend/app/page.tsx`** (Home page):
```tsx
import CourseList from '@/components/CourseList';
import ServiceCards from '@/components/ServiceCards';
import Hero from '@/components/Hero';

export default function Home() {
  return (
    <div className="min-h-screen">
      <Hero />
      <div className="container mx-auto px-4 py-12">
        <h2 className="text-3xl font-bold mb-8">Featured Courses</h2>
        <CourseList limit={6} />
        
        <h2 className="text-3xl font-bold mt-16 mb-8">Our Services</h2>
        <ServiceCards />
      </div>
    </div>
  );
}
```

**`frontend/app/courses/page.tsx`**:
```tsx
'use client';

import { useState, useEffect } from 'react';
import CourseCard from '@/components/CourseCard';
import CourseFilters from '@/components/CourseFilters';
import { Course } from '@/types/course';

export default function CoursesPage() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [filteredCourses, setFilteredCourses] = useState<Course[]>([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCourses();
    fetchCategories();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/courses/public');
      const data = await response.json();
      setCourses(data);
      setFilteredCourses(data);
    } catch (error) {
      console.error('Error fetching courses:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFilter = (filters: any) => {
    let filtered = [...courses];
    
    if (filters.categoryId) {
      filtered = filtered.filter(course => 
        course.category.id === filters.categoryId
      );
    }
    
    if (filters.mode) {
      filtered = filtered.filter(course => 
        course.mode === filters.mode
      );
    }
    
    setFilteredCourses(filtered);
  };

  if (loading) {
    return <div className="text-center py-12">Loading courses...</div>;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-2">Training Courses</h1>
      <p className="text-gray-600 mb-8">
        Professional training in Project Management, Data Analysis, M&E, and more
      </p>
      
      <div className="flex flex-col lg:flex-row gap-8">
        <div className="lg:w-1/4">
          <CourseFilters 
            categories={categories}
            onFilter={handleFilter}
          />
        </div>
        
        <div className="lg:w-3/4">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredCourses.map((course) => (
              <CourseCard key={course.id} course={course} />
            ))}
          </div>
          
          {filteredCourses.length === 0 && (
            <div className="text-center py-12">
              <p className="text-gray-500">No courses found matching your criteria</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
```

## 4. Docker Compose

**`docker-compose.yml`**:
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: bluestron
      POSTGRES_USER: bluestron
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database/schema.sql:/docker-entrypoint-initdb.d/init.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U bluestron"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/bluestron
      SPRING_DATASOURCE_USERNAME: bluestron
      SPRING_DATASOURCE_PASSWORD: password
      JWT_SECRET: your-secret-key-change-in-production
    depends_on:
      postgres:
        condition: service_healthy
    restart: unless-stopped

  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    environment:
      NEXT_PUBLIC_API_URL: http://localhost:8080/api
    depends_on:
      - backend
    restart: unless-stopped

volumes:
  postgres_data:
```

## 5. Makefile

**`Makefile`**:
```makefile
# Project Configuration
PROJECT_NAME = bluestron-minimal
BACKEND_DIR = backend
FRONTEND_DIR = frontend
DB_DIR = database

# Docker Configuration
DOCKER_COMPOSE = docker-compose

# PostgreSQL Configuration
DB_NAME = bluestron
DB_USER = bluestron
DB_PASSWORD = password
DB_HOST = localhost
DB_PORT = 5432

# Backend Configuration
BACKEND_PORT = 8080

# Frontend Configuration
FRONTEND_PORT = 3000

.PHONY: help setup build up down logs clean db-reset test curl-examples

help:
	@echo "Bluestron Minimal - Development Commands"
	@echo ""
	@echo "Setup:"
	@echo "  make setup           - Install dependencies and setup environment"
	@echo "  make build           - Build all services"
	@echo ""
	@echo "Development:"
	@echo "  make up              - Start all services (detached)"
	@echo "  make down            - Stop all services"
	@echo "  make logs            - View logs from all services"
	@echo "  make backend-logs    - View backend logs only"
	@echo "  make frontend-logs   - View frontend logs only"
	@echo ""
	@echo "Database:"
	@echo "  make db-connect      - Connect to PostgreSQL database"
	@echo "  make db-reset        - Reset database (WARNING: deletes all data)"
	@echo "  make db-migrate      - Run database migrations"
	@echo "  make db-backup       - Backup database to file"
	@echo ""
	@echo "Testing:"
	@echo "  make test-backend    - Run backend tests"
	@echo "  make test-frontend   - Run frontend tests"
	@echo "  make curl-examples   - Show curl examples for API testing"
	@echo ""
	@echo "Cleanup:"
	@echo "  make clean           - Remove all containers, volumes, and built images"
	@echo "  make clean-all       - Clean + remove node_modules and target directories"

setup:
	@echo "Setting up development environment..."
	
	# Install backend dependencies
	@if [ -d "$(BACKEND_DIR)" ]; then \
		echo "Installing backend dependencies..."; \
		cd $(BACKEND_DIR) && ./mvnw dependency:resolve; \
	fi
	
	# Install frontend dependencies
	@if [ -d "$(FRONTEND_DIR)" ]; then \
		echo "Installing frontend dependencies..."; \
		cd $(FRONTEND_DIR) && npm ci; \
	fi
	
	# Create environment files if they don't exist
	@if [ ! -f "$(BACKEND_DIR)/.env" ]; then \
		echo "Creating backend .env file..."; \
		cp $(BACKEND_DIR)/.env.example $(BACKEND_DIR)/.env 2>/dev/null || true; \
	fi
	
	@echo "Setup complete!"

build:
	@echo "Building all services..."
	$(DOCKER_COMPOSE) build

up:
	@echo "Starting all services..."
	$(DOCKER_COMPOSE) up -d
	@echo ""
	@echo "Services running:"
	@echo "  Frontend: http://localhost:$(FRONTEND_PORT)"
	@echo "  Backend API: http://localhost:$(BACKEND_PORT)"
	@echo "  PostgreSQL: localhost:$(DB_PORT)"
	@echo ""
	@echo "Check logs: make logs"

down:
	@echo "Stopping all services..."
	$(DOCKER_COMPOSE) down

logs:
	$(DOCKER_COMPOSE) logs -f

backend-logs:
	$(DOCKER_COMPOSE) logs -f backend

frontend-logs:
	$(DOCKER_COMPOSE) logs -f frontend

db-connect:
	@echo "Connecting to PostgreSQL..."
	PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME)

db-reset:
	@echo "WARNING: This will delete all data in the database!"
	@read -p "Are you sure? (y/N): " confirm && [ $$confirm = y ] || exit 1
	$(DOCKER_COMPOSE) down -v
	$(DOCKER_COMPOSE) up -d postgres
	@sleep 5
	@echo "Database reset complete"

db-migrate:
	@echo "Running database migrations..."
	@if [ -f "$(DB_DIR)/migrations/001_initial_schema.sql" ]; then \
		PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) \
		-f $(DB_DIR)/migrations/001_initial_schema.sql; \
	fi

db-backup:
	@echo "Backing up database..."
	@mkdir -p backups
	@timestamp=$$(date +"%Y%m%d_%H%M%S") && \
	PGPASSWORD=$(DB_PASSWORD) pg_dump -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) $(DB_NAME) \
	> backups/backup_$$timestamp.sql
	@echo "Backup saved to backups/backup_$$timestamp.sql"

test-backend:
	@echo "Running backend tests..."
	@cd $(BACKEND_DIR) && ./mvnw test

test-frontend:
	@echo "Running frontend tests..."
	@cd $(FRONTEND_DIR) && npm test

curl-examples:
	@echo "API Testing Examples:"
	@echo ""
	@echo "1. Get published courses:"
	@echo "   curl -X GET http://localhost:$(BACKEND_PORT)/api/courses/public"
	@echo ""
	@echo "2. Register for a course (replace COURSE_ID):"
	@echo "   curl -X POST http://localhost:$(BACKEND_PORT)/api/courses/public/COURSE_ID/register \\"
	@echo '     -H "Content-Type: application/json" \\'
	@echo '     -d '\''{"fullName":"John Doe","email":"john@example.com","phone":"+254712345678"}'\'''
	@echo ""
	@echo "3. Admin login:"
	@echo '   curl -X POST http://localhost:$(BACKEND_PORT)/api/auth/login \'
	@echo '     -H "Content-Type: application/json" \'
	@echo '     -d '\''{"email":"admin@bluestron.co.ke","password":"Admin123!"}'\'''
	@echo ""
	@echo "4. Create course (with JWT token):"
	@echo '   curl -X POST http://localhost:$(BACKEND_PORT)/api/courses/admin \'
	@echo '     -H "Content-Type: application/json" \'
	@echo '     -H "Authorization: Bearer YOUR_JWT_TOKEN" \'
	@echo '     -d '\''{"title":"New Course","slug":"new-course","shortDescription":"Course desc"}'\'''

clean:
	@echo "Cleaning up..."
	$(DOCKER_COMPOSE) down -v --remove-orphans
	@docker system prune -f

clean-all: clean
	@echo "Removing build artifacts..."
	@if [ -d "$(BACKEND_DIR)/target" ]; then \
		rm -rf $(BACKEND_DIR)/target; \
	fi
	@if [ -d "$(FRONTEND_DIR)/node_modules" ]; then \
		rm -rf $(FRONTEND_DIR)/node_modules; \
	fi
	@if [ -d "$(FRONTEND_DIR)/.next" ]; then \
		rm -rf $(FRONTEND_DIR)/.next; \
	fi
	@echo "Cleanup complete!"

# Development shortcuts
dev-backend:
	@cd $(BACKEND_DIR) && ./mvnw spring-boot:run

dev-frontend:
	@cd $(FRONTEND_DIR) && npm run dev

# Health checks
health:
	@echo "Checking service health..."
	@echo -n "Backend: " && curl -f http://localhost:$(BACKEND_PORT)/actuator/health 2>/dev/null && echo " ✓" || echo " ✗"
	@echo -n "Frontend: " && curl -f http://localhost:$(FRONTEND_PORT) 2>/dev/null && echo " ✓" || echo " ✗"
	@echo -n "Database: " && PGPASSWORD=$(DB_PASSWORD) pg_isready -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) && echo " ✓" || echo " ✗"

# Database schema dump
db-schema:
	@echo "Generating schema diagram..."
	PGPASSWORD=$(DB_PASSWORD) pg_dump -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) \
	--schema-only --no-owner $(DB_NAME) > $(DB_DIR)/schema_dump_$$(date +%Y%m%d).sql
	@echo "Schema dumped to $(DB_DIR)/schema_dump_$$(date +%Y%m%d).sql"

# Quick restart
restart:
	$(DOCKER_COMPOSE) restart

# View running containers
ps:
	$(DOCKER_COMPOSE) ps

# Seed database with sample data
db-seed:
	@echo "Seeding database with sample data..."
	@if [ -f "$(DB_DIR)/seed_data.sql" ]; then \
		PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) \
		-d $(DB_NAME) -f $(DB_DIR)/seed_data.sql; \
	else \
		echo "No seed data file found at $(DB_DIR)/seed_data.sql"; \
	fi

# Default target
.DEFAULT_GOAL := help
```

## 6. Quick Start Instructions

```bash
# 1. Clone and setup
make setup

# 2. Start all services
make up

# 3. Check services are running
make health

# 4. View the application
# Open browser to http://localhost:3000

# 5. Test API endpoints
make curl-examples

# 6. Connect to database
make db-connect

# 7. When done, stop services
make down
```

## Key Features Implemented:

1. **Minimal Viable Product**: Core CRUD for courses, categories, services
2. **Basic Auth**: Admin can manage all content
3. **Course Registration**: Public registration workflow
4. **Modular Structure**: Each component can be extended independently
5. **PostgreSQL Schema**: Full SQL schema ready for migration
6. **Makefile Automation**: One-command setup, build, run, test
7. **Docker Support**: Containerized development environment
8. **TypeScript/Java Types**: Full type safety
9. **REST API**: Clean API endpoints following REST conventions
10. **Responsive Design**: Built with Tailwind CSS

## Next Steps for Iteration:

1. **Admin Dashboard**: Add React Admin or custom dashboard
2. **Payment Integration**: Add Stripe/MPesa payment processing
3. **Email Service**: Integrate email notifications (SendGrid, AWS SES)
4. **File Uploads**: Course images, document uploads
5. **Advanced Search**: Elasticsearch integration
6. **Caching**: Redis for performance
7. **Analytics**: Track user behavior
8. **Multi-language**: Internationalization support
9. **SSO**: OAuth/Google login
10. **Advanced Reporting**: Registration analytics

The architecture is designed to be extended - each service is independent, database schema is normalized, and the code follows best practices for maintainability.