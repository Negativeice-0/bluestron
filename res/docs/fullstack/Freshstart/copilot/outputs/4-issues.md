## Bluestron scaffold — Spring Boot 3.5.9 (Maven), Next.js (TypeScript), Postgres, Tailwind v4 defaults

You’re right—I overreached on Tailwind config and missed the featured courses carousel and your preference for using plain `<img>` over `next/image`. We’ll keep Tailwind v4 defaults (no custom config files), avoid Docker/Flyway/Jenkins, and build block‑by‑block with minimal complexity. Admin functionality will be explicit and simple: CRUD endpoints and basic admin pages, with a lightweight guard you can swap later for proper auth.

Below is Block 1 (Categories) and the homepage with a featured courses carousel stub, all using Tailwind v4 defaults. Then I outline the admin plan and Block 2 (Courses) so we stack cleanly.

---

## Monorepo structure

```
bluestron/
├─ backend/
│  ├─ src/main/java/co/ke/bluestron/...
│  ├─ src/main/resources/application.yml
│  ├─ pom.xml
│  ├─ migrations/
│  │  ├─ 001_init_schema.sql
│  │  ├─ 002_categories_table.sql
│  │  └─ 003_seed_categories.sql
├─ frontend/
│  ├─ app/...
│  ├─ components/...
│  ├─ lib/...
│  ├─ public/...
│  ├─ package.json
│  ├─ tsconfig.json
│  ├─ next.config.ts   (empty as you requested)
├─ Makefile
├─ README.md
```

---

## Backend — Spring Boot 3.5.9 (Maven), Postgres

#### backend/pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>co.ke.bluestron</groupId>
  <artifactId>bsapi</artifactId>
  <version>0.0.1</version>
  <name>bsapi</name>
  <description>Bluestron API</description>

  <properties>
    <java.version>17</java.version>
    <spring.boot.version>3.5.9</spring.boot.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <!-- Minimal set: web, JPA, Postgres, validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.7.3</version>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

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

#### backend/src/main/resources/application.yml
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

#### backend/src/main/java/co/ke/bluestron/BluestronApplication.java
```java
package co.ke.bluestron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Allegory: The conductor—starts the symphony and keeps tempo.
@SpringBootApplication
public class BluestronApplication {
    public static void main(String[] args) {
        SpringApplication.run(BluestronApplication.class, args);
    }
}
```

---

## Database migrations (raw SQL, IF NOT EXISTS)

#### backend/migrations/001_init_schema.sql
```sql
-- Allegory: Build the hall before placing shelves.
CREATE SCHEMA IF NOT EXISTS bluestron;
SET search_path TO bluestron;
```

#### backend/migrations/002_categories_table.sql
```sql
SET search_path TO bluestron;

-- Allegory: Shelves for categories—labeled and indexed.
CREATE TABLE IF NOT EXISTS categories (
  id          BIGSERIAL PRIMARY KEY,
  slug        TEXT UNIQUE NOT NULL,
  name        TEXT NOT NULL,
  description TEXT,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_categories_slug ON categories(slug);
CREATE INDEX IF NOT EXISTS idx_categories_name ON categories(name);
```

#### backend/migrations/003_seed_categories.sql
```sql
SET search_path TO bluestron;

-- Allegory: Stock the shelves with familiar sections.
INSERT INTO categories (slug, name, description)
VALUES
  ('project-management-me', 'Project Management & M&E', 'PM, M&E, evaluations, USAID rules'),
  ('data-management-analysis', 'Data Management & Analysis', 'Excel, Power BI, SPSS, Stata, R, SAS, CSPro, cybersecurity'),
  ('gis-it', 'GIS & IT / Mobile Data Collection', 'GIS in M&E, spatial analysis, mobile data collection'),
  ('management-hr', 'Management & Administration / HR', 'HR planning, BPM, supervisory skills, communication'),
  ('climate-environment', 'Climate Change / Environment', 'Climate finance, ESG, environmental safeguards')
ON CONFLICT (slug) DO NOTHING;
```

---

## Backend — Categories domain

#### backend/src/main/java/co/ke/bluestron/categories/Category.java
```java
package co.ke.bluestron.categories;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

// Allegory: A shelf record—simple, sturdy, timestamped.
@Entity
@Table(name = "categories", schema = "bluestron")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
```

#### backend/src/main/java/co/ke/bluestron/categories/CategoryRepository.java
```java
package co.ke.bluestron.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Allegory: The librarian—finds shelves by label.
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
```

#### backend/src/main/java/co/ke/bluestron/categories/CategoryDTO.java
```java
package co.ke.bluestron.categories;

import jakarta.validation.constraints.NotBlank;

// Allegory: The index card—clean, minimal, validated.
public record CategoryDTO(
        @NotBlank String slug,
        @NotBlank String name,
        String description
) {}
```

#### backend/src/main/java/co/ke/bluestron/categories/CategoryResponse.java
```java
package co.ke.bluestron.categories;

// Allegory: The public label—what visitors read.
public record CategoryResponse(
        Long id,
        String slug,
        String name,
        String description
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getSlug(), c.getName(), c.getDescription());
    }
}
```

#### backend/src/main/java/co/ke/bluestron/categories/CategoryService.java
```java
package co.ke.bluestron.categories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// Allegory: The curator—keeps shelves consistent.
@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return repo.findAll().stream().map(CategoryResponse::from).toList();
    }

    @Transactional
    public CategoryResponse create(CategoryDTO dto) {
        if (repo.existsBySlug(dto.slug())) {
            throw new IllegalArgumentException("Category slug already exists: " + dto.slug());
        }
        Category c = new Category();
        c.setSlug(dto.slug());
        c.setName(dto.name());
        c.setDescription(dto.description());
        Category saved = repo.save(c);
        return CategoryResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getBySlug(String slug) {
        Category c = repo.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("Not found: " + slug));
        return CategoryResponse.from(c);
    }
}
```

#### backend/src/main/java/co/ke/bluestron/categories/CategoryController.java
```java
package co.ke.bluestron.categories;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Allegory: The front desk—welcomes requests, returns neat answers.
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CategoryResponse> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
```

---

## Makefile — migrations, backend run, verification

#### Makefile
```make
DB_NAME=bsdb
DB_USER=bsdbu
DB_PASS=bsdbp2Pass&1!
DB_HOST=localhost
DB_PORT=5432

.PHONY: db-migrate db-seed backend-run backend-build verify-categories frontend-dev frontend-build

db-migrate:
	@echo "Applying schema and categories table"
	psql -h $(DB_HOST) -U $(DB_USER) -d $(DB_NAME) -f backend/migrations/001_init_schema.sql
	psql -h $(DB_HOST) -U $(DB_USER) -d $(DB_NAME) -f backend/migrations/002_categories_table.sql

db-seed:
	@echo "Seeding categories"
	psql -h $(DB_HOST) -U $(DB_USER) -d $(DB_NAME) -f backend/migrations/003_seed_categories.sql

backend-run:
	cd backend && mvn spring-boot:run

backend-build:
	cd backend && mvn -q -DskipTests package

verify-categories:
	@echo "GET /api/categories"
	curl -s http://localhost:8080/api/categories | jq '.'
	@echo "GET /api/categories/project-management-me"
	curl -s http://localhost:8080/api/categories/project-management-me | jq '.'

frontend-dev:
	cd frontend && npm run dev

frontend-build:
	cd frontend && npm run build
```

---

## Frontend — Next.js (TypeScript), Tailwind v4 defaults, Bluestron branding

- Tailwind v4 defaults: no config files. Use utility classes directly.
- Use `<img>` tags per your note (not `next/image`).
- Strict TypeScript: no `any`/`unknown` in signatures.

#### frontend/package.json
```json
{
  "name": "bluestron-frontend",
  "private": true,
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint"
  },
  "dependencies": {
    "next": "14.2.5",
    "react": "18.2.0",
    "react-dom": "18.2.0",
    "tailwindcss": "4.0.0"
  },
  "devDependencies": {
    "@types/node": "^20.11.30",
    "@types/react": "^18.2.66",
    "typescript": "^5.6.2",
    "eslint": "^8.57.0",
    "eslint-config-next": "^14.2.5"
  }
}
```

#### frontend/tsconfig.json
```json
{
  "compilerOptions": {
    "target": "ES2020",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "strict": true,
    "jsx": "preserve",
    "baseUrl": ".",
    "paths": { "@/*": ["./*"] },
    "noImplicitAny": true,
    "noUncheckedIndexedAccess": true
  }
}
```

#### frontend/next.config.ts
```ts
// Intentionally empty per your instruction.
export default {};
```

#### frontend/app/layout.tsx
```tsx
import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'Bluestron — Training, Research & Software Development',
  description: 'Professional Training, Research & Software Development in Data, M&E, Management',
  icons: [{ rel: 'icon', url: '/favicon.ico' }]
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  // Allegory: The frame—consistent chrome around every exhibit.
  return (
    <html lang="en">
      <body className="min-h-screen bg-[#E6F2FA] text-[#062B4F]">
        <header className="border-b border-slate-200">
          <nav className="mx-auto max-w-5xl flex gap-6 p-4 text-sm">
            <a href="/" className="text-[#0A4D8C] font-semibold">Bluestron</a>
            <a href="/courses">Courses</a>
            <a href="/services">Services</a>
            <a href="/contact">Contact</a>
          </nav>
        </header>
        <main className="mx-auto max-w-5xl p-4">{children}</main>
        <footer className="mx-auto max-w-5xl p-4 border-t border-slate-200 text-xs">
          © Bluestron
        </footer>
      </body>
    </html>
  );
}
```

#### frontend/app/globals.css
```css
@import "tailwindcss";

/* Allegory: A quiet canvas—let the content breathe. */
:root { color-scheme: light; }
```

#### frontend/lib/api.ts
```ts
const BASE_URL: string = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';

export type Category = {
  id: number;
  slug: string;
  name: string;
  description: string | null;
};

async function getJson<T>(url: string): Promise<T> {
  const res: Response = await fetch(url, { cache: 'no-store' });
  if (!res.ok) {
    const message: string = `Request failed: ${res.status}`;
    throw new Error(message);
  }
  const data: unknown = await res.json();
  return data as T;
}

export async function fetchCategories(): Promise<Category[]> {
  return getJson<Category[]>(`${BASE_URL}/api/categories`);
}

export async function fetchCategory(slug: string): Promise<Category> {
  return getJson<Category>(`${BASE_URL}/api/categories/${encodeURIComponent(slug)}`);
}
```

#### frontend/components/FeaturedCarousel.tsx
```tsx
'use client';

import { useEffect, useRef, useState } from 'react';

type Slide = { title: string; subtitle: string; imageUrl: string; href: string };

export function FeaturedCarousel({ slides }: { slides: readonly Slide[] }) {
  // Allegory: A gentle conveyor—courses glide by, inviting attention.
  const [index, setIndex] = useState<number>(0);
  const timerRef = useRef<number | null>(null);

  useEffect(() => {
    timerRef.current = window.setInterval(() => {
      setIndex((prev) => (prev + 1) % slides.length);
    }, 4000);
    return () => {
      if (timerRef.current !== null) window.clearInterval(timerRef.current);
    };
  }, [slides.length]);

  const current: Slide = slides[index];

  return (
    <div className="relative overflow-hidden rounded-lg border border-slate-200 bg-white">
      <div className="grid grid-cols-1 md:grid-cols-[1fr_320px]">
        <div className="p-6">
          <h3 className="text-lg font-semibold text-[#0A4D8C]">{current.title}</h3>
          <p className="mt-1 text-sm">{current.subtitle}</p>
          <a href={current.href} className="mt-3 inline-block rounded-md bg-[#1E88E5] px-3 py-2 text-white text-sm">
            View course
          </a>
        </div>
        <div className="h-48 md:h-full">
          <img
            src={current.imageUrl}
            alt={current.title}
            className="h-full w-full object-cover"
          />
        </div>
      </div>
      <div className="absolute bottom-2 left-2 flex gap-2">
        {slides.map((_, i) => (
          <button
            key={i}
            aria-label={`Go to slide ${i + 1}`}
            onClick={() => setIndex(i)}
            className={`h-2 w-2 rounded-full ${i === index ? 'bg-[#1E88E5]' : 'bg-slate-300'}`}
          />
        ))}
      </div>
    </div>
  );
}
```

#### frontend/app/page.tsx (Home with featured carousel + categories)
```tsx
import { fetchCategories } from '@/lib/api';
import { FeaturedCarousel } from '@/components/FeaturedCarousel';

export default async function HomePage() {
  const categories = await fetchCategories();

  const slides = [
    {
      title: 'Project Management for Development Professionals',
      subtitle: 'Plan, execute, and evaluate development projects with confidence.',
      imageUrl: 'https://images.unsplash.com/photo-1529333166437-7750a6dd5a70?w=1200&q=80',
      href: '/courses?category=project-management-me'
    },
    {
      title: 'Advanced Data Analysis with Power BI',
      subtitle: 'Transform data into decisions with dashboards and visuals.',
      imageUrl: 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=1200&q=80',
      href: '/courses?category=data-management-analysis'
    },
    {
      title: 'GIS Mapping and Spatial Analysis',
      subtitle: 'Map impact and monitor change with spatial intelligence.',
      imageUrl: 'https://images.unsplash.com/photo-1502920917128-1aa500764b6a?w=1200&q=80',
      href: '/courses?category=gis-it'
    }
  ] as const;

  return (
    <section className="space-y-6">
      <div className="rounded-lg bg-white p-6 shadow-sm">
        <h1 className="text-2xl font-bold text-[#0A4D8C]">Bluestron Training</h1>
        <p className="mt-2 text-sm">
          Professional Training, Research & Software Development in Data, M&E, Management
        </p>
      </div>

      <FeaturedCarousel slides={slides} />

      <div>
        <h2 className="text-xl font-semibold">Categories</h2>
        <ul className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
          {categories.map((c) => (
            <li key={c.id} className="rounded-md border border-slate-200 bg-white p-4">
              <a
                href={`/courses?category=${encodeURIComponent(c.slug)}`}
                className="text-[#1E88E5] font-medium"
              >
                {c.name}
              </a>
              <p className="text-xs mt-1">{c.description ?? '—'}</p>
            </li>
          ))}
        </ul>
      </div>
    </section>
  );
}
```

#### frontend/app/courses/page.tsx
```tsx
import { fetchCategories } from '@/lib/api';

type Params = { category?: string };

export default async function CoursesPage({ searchParams }: { searchParams?: Params }) {
  const categories = await fetchCategories();
  const selected: string | undefined = searchParams?.category;

  return (
    <div className="grid grid-cols-1 md:grid-cols-[240px_1fr] gap-6">
      <aside className="space-y-2">
        <h3 className="font-semibold">Categories</h3>
        <ul className="space-y-1">
          {categories.map((c) => (
            <li key={c.id}>
              <a
                href={`/courses?category=${encodeURIComponent(c.slug)}`}
                className="text-sm text-[#0A4D8C] hover:underline"
              >
                {c.name}
              </a>
            </li>
          ))}
        </ul>
      </aside>
      <section>
        <h2 className="text-lg font-semibold">Courses</h2>
        {!selected ? (
          <p className="text-sm">Select a category to view courses.</p>
        ) : (
          <p className="text-sm">
            Selected category: <strong>{selected}</strong> — courses will appear here in Block 2.
          </p>
        )}
      </section>
    </div>
  );
}
```

---

## Admin functionality plan (minimal, explicit, expandable)

- **Goal:** Full CRUD for Categories and Courses, plus Registrations and Services pages content.
- **Backend:** REST endpoints under `/api/admin/*` for create/update/delete. Keep it simple now—guard with a static admin token header (e.g., `X-Admin-Token`) read from environment. Later, swap for proper auth (JWT/OAuth).
- **Frontend:** Admin pages under `/admin/*` with forms and tables. Use fetch with the header token. No extra libraries.

### Lightweight admin guard

#### backend/src/main/java/co/ke/bluestron/admin/AdminGuard.java
```java
package co.ke.bluestron.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AdminGuard {
    // Allegory: A simple gate—keyed lock for now, fortress later.
    private final String token = System.getenv().getOrDefault("BS_ADMIN_TOKEN", "");

    public void check(HttpServletRequest request) {
        String header = request.getHeader("X-Admin-Token");
        if (token.isEmpty() || header == null || !header.equals(token)) {
            throw new SecurityException("Unauthorized");
        }
    }
}
```

#### Example usage in an admin controller (Categories)
```java
// backend/src/main/java/co/ke/bluestron/categories/AdminCategoryController.java
package co.ke.bluestron.categories;

import co.ke.bluestron.admin.AdminGuard;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService service;
    private final AdminGuard guard;

    public AdminCategoryController(CategoryService service, AdminGuard guard) {
        this.service = service;
        this.guard = guard;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(HttpServletRequest req, @Valid @RequestBody CategoryDTO dto) {
        guard.check(req);
        return ResponseEntity.ok(service.create(dto));
    }

    // Update/delete can be added similarly in Block 2.
}
```

### Admin UI stub

#### frontend/app/admin/page.tsx
```tsx
export default function AdminHome() {
  // Allegory: The control room—switches and dials for content.
  return (
    <section className="space-y-4">
      <h1 className="text-xl font-semibold text-[#0A4D8C]">Admin</h1>
      <ul className="list-disc pl-5 text-sm">
        <li><a href="/admin/categories">Manage Categories</a></li>
        <li><a href="/admin/courses">Manage Courses</a></li>
        <li><a href="/admin/registrations">Manage Registrations</a></li>
        <li><a href="/admin/services">Manage Services Pages</a></li>
      </ul>
    </section>
  );
}
```

#### frontend/app/admin/categories/page.tsx
```tsx
'use client';

import { useState } from 'react';

type FormState = { slug: string; name: string; description: string };

export default function AdminCategoriesPage() {
  const [form, setForm] = useState<FormState>({ slug: '', name: '', description: '' });
  const [message, setMessage] = useState<string>('');

  async function submit() {
    const token: string = process.env.NEXT_PUBLIC_ADMIN_TOKEN ?? '';
    const res: Response = await fetch('http://localhost:8080/api/admin/categories', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Admin-Token': token
      },
      body: JSON.stringify(form)
    });
    if (res.ok) {
      setMessage('Category created.');
      setForm({ slug: '', name: '', description: '' });
    } else {
      setMessage(`Failed: ${res.status}`);
    }
  }

  return (
    <section className="space-y-4">
      <h2 className="text-lg font-semibold">Create Category</h2>
      <div className="grid gap-2 max-w-md">
        <input
          className="border border-slate-300 rounded p-2 text-sm bg-white"
          placeholder="Slug"
          value={form.slug}
          onChange={(e) => setForm({ ...form, slug: e.target.value })}
        />
        <input
          className="border border-slate-300 rounded p-2 text-sm bg-white"
          placeholder="Name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
        />
        <textarea
          className="border border-slate-300 rounded p-2 text-sm bg-white"
          placeholder="Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
        />
        <button
          onClick={submit}
          className="rounded bg-[#1E88E5] text-white text-sm px-3 py-2"
        >
          Save
        </button>
        {message && <p className="text-xs">{message}</p>}
      </div>
    </section>
  );
}
```

---

## Verification loop (Categories)

1. **Apply migrations and seed:**
   ```
   make db-migrate
   make db-seed
   ```

2. **Run backend:**
   ```
   cd backend && mvn spring-boot:run
   ```

3. **Verify via curl:**
   ```
   make verify-categories
   # or
   curl -s http://localhost:8080/api/categories | jq '.'
   curl -s http://localhost:8080/api/categories/project-management-me | jq '.'
   ```

4. **Run frontend:**
   ```
   cd frontend && npm install
   make frontend-dev
   ```
   - Visit `http://localhost:3000` → Home shows featured carousel + categories.
   - Visit `http://localhost:3000/courses?category=project-management-me` → selection reflects.

5. **Admin create category (UI):**
   - Set `NEXT_PUBLIC_ADMIN_TOKEN` in your frontend env (or `.env.local`).
   - Set `BS_ADMIN_TOKEN` in backend environment.
   - Visit `http://localhost:3000/admin/categories` → create a category.

---

## Block 2 preview — Courses (dependent on Categories)

We’ll add next:
- `courses` table with FK to `categories(id)`; seed with the list from your document.
- Backend: `Course` entity, repository, DTOs, service, controllers:
  - Public: `/api/courses`, `/api/courses?category=slug`, `/api/courses/{slug}`
  - Admin: `/api/admin/courses` (create/update/delete)
- Frontend:
  - Courses listing under selected category (tiles with thumbnail, title, short description, “Register” button).
  - Course detail page with full description and a registration form (inline modal or page).
  - Featured courses carousel pulls from a “featured” flag.
- Verification: curl for list/filter/detail + UI checks.

---

## Direct answers to your concerns

- **Tailwind v4 defaults:** Honored—no custom config files. We used utility classes only.
- **Featured courses carousel:** Implemented as `FeaturedCarousel` with `<img>` tags and simple autoplay.
- **`img` vs `next/image`:** We used `<img>` per your note. If you want `next/image` later for optimization, we can swap easily.
- **Admin functionality:** Planned and partially implemented (Categories create). We’ll expand to full CRUD and Courses in Block 2, then Registrations and Services.
- **Complexity:** Minimal—no Docker, no Flyway, no Jenkins. Raw SQL migrations via Makefile, simple admin guard via header token.

---

## One question to keep us smooth

- Do you want the registration form to be inline on the course detail page or a modal? I’ll implement whichever you prefer in Block 2.