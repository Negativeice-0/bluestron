# Bluestron modular MVP scaffold (Java 21 + Spring Boot 3 + Next.js 14)

You gave me a clear north star: training first, clean categories→courses→register, and distinct service pages—plus admin CRUD to keep everything living and current. Here’s a reproducible monorepo scaffold that hits auth, CRUD, registration, and leaves room to iterate without drift.

---

## Monorepo layout

```
bluestron-mvp/
├─ Makefile
├─ backend/
│  ├─ pom.xml
│  ├─ src/main/java/co/ke/bluestron/
│  │  ├─ BluestronApplication.java
│  │  ├─ config/ (SecurityConfig, JwtAuthFilter, CorsConfig)
│  │  ├─ common/ (exceptions, pagination, Result<T>)
│  │  ├─ auth/ (controller, service, repo, dto, model)
│  │  ├─ category/ (controller, service, repo, dto, model)
│  │  ├─ course/ (controller, service, repo, dto, model)
│  │  ├─ registration/ (controller, service, repo, dto, model)
│  │  ├─ servicepages/ (controller, service, repo, dto, model)
│  │  └─ admin/ (controller for admin-only endpoints)
│  ├─ src/main/resources/
│  │  ├─ application.yml
│  │  └─ templates/ (email stubs if needed)
│  └─ src/test/java/... (unit tests)
├─ frontend/
│  ├─ package.json
│  ├─ next.config.js
│  ├─ tsconfig.json
│  ├─ postcss.config.js
│  ├─ tailwind.config.ts
│  └─ src/
│     ├─ app/
│     │  ├─ layout.tsx
│     │  ├─ page.tsx (Home)
│     │  ├─ about/page.tsx
│     │  ├─ courses/page.tsx
│     │  ├─ courses/[slug]/page.tsx
│     │  ├─ research/page.tsx
│     │  ├─ data-analysis/page.tsx
│     │  ├─ software-dev/page.tsx
│     │  ├─ contact/page.tsx
│     │  └─ admin/...
│     ├─ components/ (cards, forms, tables)
│     ├─ lib/fetch.ts (JWT fetch wrapper)
│     └─ styles/globals.css
└─ database/
   ├─ schema.sql
   └─ seed.sql
```

- **Focus:** Training-first UX with categories→courses→detail→register, and distinct service pages for research, data analysis, and software dev—visually subordinate but not buried.
- **Admin:** CRUD for categories, courses, registrations, service pages; role-based access (ADMIN vs USER).
- **Registration:** Inline form on course detail, confirmation page, email stub to participant and admin.

---

## Database schema and seed

### `database/schema.sql`
```sql
-- Users
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN','USER')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Categories
CREATE TABLE categories (
  id BIGSERIAL PRIMARY KEY,
  slug VARCHAR(120) UNIQUE NOT NULL,
  name VARCHAR(120) NOT NULL,
  description TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Courses
CREATE TABLE courses (
  id BIGSERIAL PRIMARY KEY,
  slug VARCHAR(160) UNIQUE NOT NULL,
  category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  short_description TEXT,
  full_description TEXT,
  duration VARCHAR(100),
  mode VARCHAR(40) CHECK (mode IN ('IN_PERSON','ONLINE','HYBRID')),
  location VARCHAR(200),
  next_date DATE,
  thumbnail_url TEXT,
  price NUMERIC(12,2),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Registrations
CREATE TABLE registrations (
  id BIGSERIAL PRIMARY KEY,
  course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  full_name VARCHAR(200) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(60),
  organization VARCHAR(255),
  role VARCHAR(120),
  preferred_date DATE,
  payment_option VARCHAR(40) CHECK (payment_option IN ('ONLINE','INVOICE')),
  status VARCHAR(40) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','CONFIRMED','CANCELLED')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Service pages (Research, Data Analysis, Software Dev)
CREATE TABLE service_pages (
  id BIGSERIAL PRIMARY KEY,
  slug VARCHAR(120) UNIQUE NOT NULL,
  title VARCHAR(200) NOT NULL,
  content_md TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_courses_category ON courses(category_id);
CREATE INDEX idx_courses_next_date ON courses(next_date);
CREATE INDEX idx_registrations_course ON registrations(course_id);
```

### `database/seed.sql`
```sql
-- Admin user (bcrypt hash for 'admin123' placeholder)
INSERT INTO users (email, password_hash, full_name, role)
VALUES ('admin@bluestron.co.ke', '$2a$10$QnYQwQwQwQwQwQwQwQwQwO8o8o8o8o8o8o8o8o8o8o8o8o8o8o8', 'Bluestron Admin', 'ADMIN');

-- Sample category
INSERT INTO categories (slug, name, description)
VALUES ('project-management-me', 'Project Management / M&E', 'Courses in project management and monitoring & evaluation');

-- Sample course
INSERT INTO courses (slug, category_id, title, short_description, full_description, duration, mode, location, next_date, thumbnail_url, price)
VALUES (
  'project-management-for-development-professionals',
  (SELECT id FROM categories WHERE slug='project-management-me'),
  'Project Management for Development Professionals',
  'Core PM skills tailored for development contexts.',
  'Full description with learning outcomes, tools, and case studies.',
  '5 days',
  'IN_PERSON',
  'Nairobi',
  CURRENT_DATE + INTERVAL '14 days',
  '/images/courses/pmdev.jpg',
  0
);

-- Service pages
INSERT INTO service_pages (slug, title, content_md) VALUES
('research', 'Research Services', '# Baseline, Midline, Endline\nWe deliver rigorous studies and evaluations.'),
('data-analysis', 'Data Analysis & Management', '# Data cleaning, dashboards, tools\nExcel, Power BI, SPSS, Stata, R.'),
('software-dev', 'Software Development', '# Custom web/mobile\nData collection apps, dashboards, integrations.');
```

> Replace the bcrypt hash with a real one during setup. The categories and course list reflect the PDF’s training-first structure and named categories.

---

## Root Makefile (reproducible targets)

```make
# Environment
DB_NAME=bluestron_mvp
DB_USER=postgres
DB_HOST=localhost
DB_PORT=5432

.PHONY: db-create db-drop db-schema db-seed db-reset db-psql \
        be-build be-run be-test be-curl-auth be-curl-courses be-curl-register \
        fe-install fe-dev fe-build fe-start

## Database
db-create:
	@echo "Creating DB $(DB_NAME)"; createdb -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) $(DB_NAME)

db-drop:
	@echo "Dropping DB $(DB_NAME)"; dropdb -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) --if-exists $(DB_NAME)

db-schema:
	@echo "Applying schema.sql"; psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/schema.sql

db-seed:
	@echo "Applying seed.sql"; psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/seed.sql

db-reset: db-drop db-create db-schema db-seed

db-psql:
	psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME)

## Backend
be-build:
	cd backend && mvn -q -DskipTests package

be-run:
	cd backend && mvn spring-boot:run

be-test:
	cd backend && mvn -q test

be-curl-auth:
	@echo "Login (admin)"; curl -s -X POST http://localhost:8080/api/auth/login \
	 -H "Content-Type: application/json" \
	 -d '{"email":"admin@bluestron.co.ke","password":"admin123"}' | jq

be-curl-courses:
	@echo "List courses"; curl -s http://localhost:8080/api/courses | jq

be-curl-register:
	@echo "Register sample"; curl -s -X POST http://localhost:8080/api/registrations \
	 -H "Content-Type: application/json" \
	 -d '{"courseSlug":"project-management-for-development-professionals","fullName":"Jane Doe","email":"jane@example.com","phone":"+254700000000","organization":"Org","role":"Analyst","preferredDate":null,"paymentOption":"INVOICE"}' | jq

## Frontend
fe-install:
	cd frontend && npm install

fe-dev:
	cd frontend && npm run dev

fe-build:
	cd frontend && npm run build

fe-start:
	cd frontend && npm run start
```

---

## Backend (Spring Boot 3, Java 21)

### `backend/src/main/resources/application.yml`
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:bluestron_mvp}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:}
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate.jdbc.time_zone: UTC

app:
  jwt:
    secret: ${JWT_SECRET:change-me}
    issuer: bluestron
    expirationMinutes: 120

logging:
  level:
    root: INFO
    org.springframework.security: INFO
```

### Security config (JWT, stateless, CORS)

- **Principles:** stateless sessions, bcrypt hashing, role-based access (ADMIN vs USER), DTO-only controllers.
- **Endpoints:** `/api/auth/**` public; `/api/courses` public read; `/api/registrations` public create; `/api/admin/**` requires ADMIN.

```java
// backend/src/main/java/co/ke/bluestron/config/SecurityConfig.java
package co.ke.bluestron.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwt) throws Exception {
  http.csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(sm -> sm.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/courses/**", "/api/service-pages/**").permitAll()
        .requestMatchers("/api/registrations/**").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
  return http.build();
}

@Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
@Bean AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception { return cfg.getAuthenticationManager(); }
```

### Auth DTOs and controller (records, immutability)

```java
// backend/src/main/java/co/ke/bluestron/auth/dto/LoginRequest.java
package co.ke.bluestron.auth.dto;
public record LoginRequest(String email, String password) {}

// backend/src/main/java/co/ke/bluestron/auth/dto/LoginResponse.java
package co.ke.bluestron.auth.dto;
public record LoginResponse(String token, String role, String fullName) {}
```

```java
// backend/src/main/java/co/ke/bluestron/auth/AuthController.java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;
  public AuthController(AuthService authService) { this.authService = authService; }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest req) {
    return authService.login(req);
  }
}
```

### Course and registration DTOs

```java
// backend/src/main/java/co/ke/bluestron/course/dto/CourseListItem.java
public record CourseListItem(String slug, String title, String shortDescription, String categorySlug, String mode, String location, LocalDate nextDate, String thumbnailUrl) {}

public record CourseDetail(
  String slug, String title, String fullDescription, String duration, String mode, String location,
  LocalDate nextDate, String thumbnailUrl, BigDecimal price, String categorySlug
) {}
```

```java
// backend/src/main/java/co/ke/bluestron/registration/dto/RegistrationCreateRequest.java
public record RegistrationCreateRequest(
  @NotBlank String courseSlug,
  @NotBlank String fullName,
  @Email String email,
  String phone,
  String organization,
  String role,
  LocalDate preferredDate,
  @NotBlank String paymentOption
) {}

public record RegistrationResponse(Long id, String status, String courseSlug, String fullName, String email) {}
```

### Controllers

```java
// backend/src/main/java/co/ke/bluestron/course/CourseController.java
@RestController
@RequestMapping("/api/courses")
public class CourseController {
  private final CourseService service;
  public CourseController(CourseService service) { this.service = service; }

  @GetMapping
  public List<CourseListItem> list(@RequestParam Optional<String> category, @RequestParam Optional<String> mode, @RequestParam Optional<LocalDate> date) {
    return service.list(category, mode, date);
  }

  @GetMapping("/{slug}")
  public CourseDetail detail(@PathVariable String slug) { return service.detail(slug); }
}
```

```java
// backend/src/main/java/co/ke/bluestron/registration/RegistrationController.java
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
  private final RegistrationService service;
  public RegistrationController(RegistrationService service) { this.service = service; }

  @PostMapping
  public RegistrationResponse create(@Valid @RequestBody RegistrationCreateRequest req) {
    return service.create(req);
  }
}
```

```java
// backend/src/main/java/co/ke/bluestron/admin/AdminController.java
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  private final AdminService admin;
  public AdminController(AdminService admin) { this.admin = admin; }

  // Categories CRUD
  @PostMapping("/categories") public CategoryResponse createCategory(@Valid @RequestBody CategoryCreateRequest req) { return admin.createCategory(req); }
  @PutMapping("/categories/{slug}") public CategoryResponse updateCategory(@PathVariable String slug, @Valid @RequestBody CategoryUpdateRequest req) { return admin.updateCategory(slug, req); }
  @DeleteMapping("/categories/{slug}") public void deleteCategory(@PathVariable String slug) { admin.deleteCategory(slug); }

  // Courses CRUD
  @PostMapping("/courses") public CourseDetail createCourse(@Valid @RequestBody CourseCreateRequest req) { return admin.createCourse(req); }
  @PutMapping("/courses/{slug}") public CourseDetail updateCourse(@PathVariable String slug, @Valid @RequestBody CourseUpdateRequest req) { return admin.updateCourse(slug, req); }
  @DeleteMapping("/courses/{slug}") public void deleteCourse(@PathVariable String slug) { admin.deleteCourse(slug); }

  // Service pages CRUD
  @PostMapping("/service-pages") public ServicePageResponse createServicePage(@Valid @RequestBody ServicePageCreateRequest req) { return admin.createServicePage(req); }
  @PutMapping("/service-pages/{slug}") public ServicePageResponse updateServicePage(@PathVariable String slug, @Valid @RequestBody ServicePageUpdateRequest req) { return admin.updateServicePage(slug, req); }
  @DeleteMapping("/service-pages/{slug}") public void deleteServicePage(@PathVariable String slug) { admin.deleteServicePage(slug); }

  // Registrations read/delete
  @GetMapping("/registrations") public List<RegistrationResponse> listRegistrations() { return admin.listRegistrations(); }
  @DeleteMapping("/registrations/{id}") public void deleteRegistration(@PathVariable Long id) { admin.deleteRegistration(id); }
}
```

### Email stub (service)

```java
// backend/src/main/java/co/ke/bluestron/registration/EmailStub.java
@Component
public class EmailStub {
  private static final Logger log = LoggerFactory.getLogger(EmailStub.class);

  public void sendParticipantConfirmation(String to, String courseTitle) {
    log.info("Email to participant {}: Registered for {}", to, courseTitle);
  }

  public void sendAdminNotification(String adminEmail, RegistrationResponse reg) {
    log.info("Email to admin {}: New registration {} for course {}", adminEmail, reg.id(), reg.courseSlug());
  }
}
```

### Unit tests (example)

```java
// backend/src/test/java/co/ke/bluestron/course/CourseServiceTest.java
class CourseServiceTest {
  @Test void list_filters_by_category_and_mode() {
    // seed in-memory repo or testcontainers (local: simple mocks)
    // assert returned items match filters
  }
}
```

---

## Frontend (Next.js 14, TypeScript, Tailwind)

### Setup

- `frontend/package.json` scripts:
```json
{
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint"
  },
  "dependencies": {
    "next": "14.x",
    "react": "18.x",
    "react-dom": "18.x",
    "tailwindcss": "^3.4.0",
    "zod": "^3.22.0"
  },
  "devDependencies": {
    "typescript": "^5.6.0",
    "eslint": "^8.57.0",
    "eslint-config-next": "14.x",
    "postcss": "^8.4.31",
    "autoprefixer": "^10.4.16"
  }
}
```

- `tailwind.config.ts`:
```ts
import type { Config } from 'tailwindcss'
export default {
  content: ['./src/**/*.{ts,tsx}'],
  theme: { extend: { colors: { bluestron: { primary: '#0A4D68', accent: '#05BFDB' } } } },
  plugins: []
} satisfies Config
```

- `src/lib/fetch.ts` (JWT wrapper):
```ts
export async function apiFetch<T>(path: string, opts: RequestInit = {}, token?: string): Promise<T> {
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}${path}`, {
    ...opts,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(opts.headers || {})
    },
    cache: 'no-store'
  });
  if (!res.ok) {
    const body = await res.text();
    throw new Error(`API ${path} failed: ${res.status} ${body}`);
  }
  return res.json();
}
```

### Pages

- **Home (`src/app/page.tsx`)**: hero value proposition, 3-box highlights (Training lead, Research, Software Dev), featured courses carousel, quick links to categories, testimonials, footer with contact/newsletter—mirroring the PDF’s sitemap.
- **Courses listing (`src/app/courses/page.tsx`)**: filters (category, mode, date), cards with thumbnail/title/short description/register CTA.
- **Course detail (`src/app/courses/[slug]/page.tsx`)**: full description, outcomes, duration, dates, register form (inline), confirmation message on submit.
- **Service pages**: `research`, `data-analysis`, `software-dev`—distinct sections with enquiry forms, visually subordinate to training.
- **Admin**: `/admin/categories`, `/admin/courses`, `/admin/service-pages`, `/admin/registrations`—tables + forms, protected by JWT (ADMIN).

#### Register form (Zod validation)

```ts
// src/app/courses/[slug]/RegisterForm.tsx
'use client'
import { useState } from 'react'
import { z } from 'zod'
import { apiFetch } from '@/lib/fetch'

const schema = z.object({
  fullName: z.string().min(2),
  email: z.string().email(),
  phone: z.string().optional(),
  organization: z.string().optional(),
  role: z.string().optional(),
  preferredDate: z.string().optional(),
  paymentOption: z.enum(['ONLINE','INVOICE'])
})

export function RegisterForm({ courseSlug }: { courseSlug: string }) {
  const [ok, setOk] = useState(false)
  const [err, setErr] = useState<string | null>(null)

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const form = new FormData(e.currentTarget)
    const data = {
      fullName: String(form.get('fullName') || ''),
      email: String(form.get('email') || ''),
      phone: String(form.get('phone') || ''),
      organization: String(form.get('organization') || ''),
      role: String(form.get('role') || ''),
      preferredDate: String(form.get('preferredDate') || ''),
      paymentOption: String(form.get('paymentOption') || 'INVOICE')
    }
    const parsed = schema.safeParse(data)
    if (!parsed.success) { setErr(parsed.error.errors[0].message); return }
    try {
      await apiFetch('/api/registrations', {
        method: 'POST',
        body: JSON.stringify({ ...parsed.data, courseSlug })
      })
      setOk(true)
    } catch (e: any) { setErr(e.message) }
  }

  if (ok) return <p className="text-green-700">Thank you for registering. Check your email for next steps.</p>

  return (
    <form onSubmit={onSubmit} className="space-y-3">
      <input name="fullName" placeholder="Full Name" className="input" aria-label="Full Name" required />
      <input name="email" type="email" placeholder="Email Address" className="input" aria-label="Email Address" required />
      <input name="phone" placeholder="Phone Number" className="input" aria-label="Phone Number" />
      <input name="organization" placeholder="Organization" className="input" aria-label="Organization" />
      <input name="role" placeholder="Role/Designation" className="input" aria-label="Role/Designation" />
      <input name="preferredDate" type="date" className="input" aria-label="Preferred Date" />
      <select name="paymentOption" className="input" aria-label="Payment Option" defaultValue="INVOICE">
        <option value="ONLINE">Online payment</option>
        <option value="INVOICE">Invoice me</option>
      </select>
      {err && <p className="text-red-700">{err}</p>}
      <button className="btn-primary" type="submit">REGISTER</button>
    </form>
  )
}
```

> Accessibility: semantic inputs, labels/aria, alt tags on images, responsive Tailwind classes—aligned with the PDF’s accessibility and responsiveness requirements.

---

## Verification loop (no silent failures)

- **DB:** `make db-reset` then `make db-psql` to inspect tables and seeds.
- **Backend:** `make be-run` then:
  - `make be-curl-auth` to get JWT.
  - `make be-curl-courses` to list seeded course.
  - `make be-curl-register` to create a registration and see email stub logs.
- **Frontend:** `make fe-install && make fe-dev`, visit:
  - `/courses` listing with filters.
  - `/courses/project-management-for-development-professionals` detail + register.
  - `/research`, `/data-analysis`, `/software-dev` service pages.
  - `/admin/*` after logging in (JWT stored client-side; protect routes).

---

## Notes on strict practices

- **DTO separation:** Controllers only accept/return DTO records; entities remain internal to repositories/services.
- **Immutability:** Java records for DTOs; services return immutable views; no entity leakage.
- **Validation:** Bean Validation on backend DTOs; Zod on frontend forms.
- **Errors:** Throw explicit exceptions with clear messages; map to `ProblemDetail` responses.
- **Auth:** BCrypt hashing, JWT with issuer/exp; roles enforced via `hasRole('ADMIN')`.
- **SEO:** Clean slugs for categories/courses; Next.js metadata; alt tags; fast load.
- **Branding:** Tailwind theme colors for Bluestron; training-first layout; distinct service pages—subordinate but visible.
- **Registration workflow:** Prominent REGISTER button, inline form, confirmation message, email stub to participant and admin—mirroring Devimpact-style clarity.

---

## Next steps (optional iteration)

- Add pagination and server-side filtering for `/api/courses`.
- Expand seed with the full course list from the PDF categories to demo breadth.
- Replace email stub with a provider (e.g., SMTP) once auth and flows are verified.
- Add admin UI tables with inline edit modals for faster content updates.

If you want, I’ll fill in the remaining backend service/repo skeletons and the admin pages next—keeping every file path explicit and testable.