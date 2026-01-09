Since we are ignoring the latest then we shall use maven with application.yaml, but can we look for shorter names (e.g bsapi for backend. bsui for frontend, bsdb for postgres and where are the postgres commands with all the grant table permissions?), then walk me through a deep dive sptep by step of the above starting from spring intializer.












### Bluestron module 0 deep dive (Maven, Spring Boot, Next.js) with short names

You want Maven with `application.yaml`, short project names—**bsapi** (backend), **bsui** (frontend), **bsdb** (Postgres)—and explicit Postgres commands with roles and grants. Here’s a step-by-step build from Spring Initializr through local verification, keeping everything minimalistic, mobile-first, and reproducible.

---

### Project naming and directory layout

- **Root structure:**
  - **bsapi/** — Spring Boot backend (Maven, Java 21)
  - **bsui/** — Next.js frontend (TypeScript, App Router)
  - **ops/** — local DB scripts, env templates, Makefile, CI configs

- **Environment files:**
  - **ops/.env.example** — template for local secrets
  - **bsapi/src/main/resources/application.yaml** — backend config
  - **bsui/.env.local** — frontend config

---

### Step 1 — Generate backend with Spring Initializr (bsapi)

- **Initializr settings:**
  - **Project:** Maven
  - **Language:** Java
  - **Spring Boot:** 3.x
  - **Group:** `co.ke.bluestron`
  - **Artifact:** `bsapi`
  - **Name:** `bsapi`
  - **Java:** 21
  - **Dependencies:** Spring Web, Spring Validation, Spring Boot Actuator, Spring Data JPA, PostgreSQL Driver, Spring Security, Lombok, Springdoc OpenAPI (optional but recommended)

- **Download and extract** into `bsapi/`.

- **Maven wrapper:** Ensure `mvnw` is present; if not, run:
  ```bash
  mvn -N io.takari:maven:wrapper
  ```

---

### Step 2 — Configure application.yaml (bsapi)

Create `bsapi/src/main/resources/application.yaml`:

```yaml
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
  sql:
    init:
      mode: never

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when_authorized

logging:
  level:
    root: INFO
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG

cors:
  allowed-origins: http://localhost:3000
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
```

- **Note:** We’ll wire a **Java-based migration runner** in `bsapi` (Module 1) to apply schema changes—no Flyway.

---

### Step 3 — Postgres setup (bsdb) with roles and grants

Create `ops/db-init.sql`:

```sql
-- Create database
CREATE DATABASE bsdb
  WITH OWNER = postgres
  ENCODING = 'UTF8'
  LC_COLLATE = 'en_US.UTF-8'
  LC_CTYPE = 'en_US.UTF-8'
  TEMPLATE = template0;

-- Create dedicated role for API
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'bsapi_user') THEN
    CREATE ROLE bsapi_user LOGIN PASSWORD 'CHANGE_ME_LOCAL';
  END IF;
END
$$;

-- Schema per bounded context (created later by migration runner, but we can pre-create)
CREATE SCHEMA IF NOT EXISTS training AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS services AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS blog AUTHORIZATION bsapi_user;

-- Grant privileges
GRANT USAGE ON SCHEMA training TO bsapi_user;
GRANT USAGE ON SCHEMA services TO bsapi_user;
GRANT USAGE ON SCHEMA blog TO bsapi_user;

-- Default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA training
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA services
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA blog
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;

-- Grant sequence usage for IDs
ALTER DEFAULT PRIVILEGES IN SCHEMA training
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA services
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA blog
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;

-- Optional: read-only role for future public reporting
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'bsapi_readonly') THEN
    CREATE ROLE bsapi_readonly LOGIN PASSWORD 'CHANGE_ME_LOCAL_RO';
  END IF;
END
$$;

GRANT CONNECT ON DATABASE bsdb TO bsapi_readonly;
GRANT USAGE ON SCHEMA training, services, blog TO bsapi_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA training
  GRANT SELECT ON TABLES TO bsapi_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA services
  GRANT SELECT ON TABLES TO bsapi_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA blog
  GRANT SELECT ON TABLES TO bsapi_readonly;
```

- **Apply script locally:**
  ```bash
  psql -U postgres -h localhost -f ops/db-init.sql
  ```

- **Local env file:** `ops/.env.example`
  ```env
  # Backend
  BSAPI_DB_PASSWORD=CHANGE_ME_LOCAL
  BSAPI_JWT_SECRET=CHANGE_ME_LOCAL_JWT
  BSAPI_GMAIL_CLIENT_ID=...
  BSAPI_GMAIL_CLIENT_SECRET=...
  BSAPI_GMAIL_REDIRECT_URI=http://localhost:8080/oauth2/callback/google
  BSAPI_GMAIL_REFRESH_TOKEN=...

  # Frontend
  NEXT_PUBLIC_API_BASE=http://localhost:8080
  NEXT_PUBLIC_PAYSTACK_PUBLIC_KEY=pk_test_...
  ```

---

### Step 4 — Backend scaffolding (bsapi)

- **Health controller:** `co.ke.bluestron.bsapi.controller.StatusController`
  ```java
  package co.ke.bluestron.bsapi.controller;

  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RestController;
  import java.util.Map;

  @RestController
  public class StatusController {
      @GetMapping("/api/status")
      public Map<String, String> status() {
          return Map.of("status", "ok");
      }
  }
  ```

- **CORS config:** `co.ke.bluestron.bsapi.config.CorsConfig`
  ```java
  package co.ke.bluestron.bsapi.config;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.web.cors.CorsConfiguration;
  import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
  import org.springframework.web.filter.CorsFilter;

  import java.util.List;

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

- **Security stub:** `co.ke.bluestron.bsapi.config.SecurityConfig`
  ```java
  package co.ke.bluestron.bsapi.config;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.security.config.Customizer;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.web.SecurityFilterChain;

  @Configuration
  public class SecurityConfig {
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/actuator/**", "/api/status", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                  .anyRequest().authenticated()
              )
              .httpBasic(Customizer.withDefaults());
          return http.build();
      }
  }
  ```

- **OpenAPI (Springdoc):** add to `pom.xml`:
  ```xml
  <dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
  </dependency>
  ```
  - Access at `/swagger-ui/index.html` and `/v3/api-docs`.

---

### Step 5 — Frontend scaffolding (bsui)

- **Create Next.js app:**
  ```bash
  npx create-next-app@latest bsui --ts --eslint --src-dir --app --tailwind --import-alias "@/*"
  ```

- **Tailwind config tokens (soft white, navy, orange):** `tailwind.config.ts`
  ```ts
  import type { Config } from 'tailwindcss'

  const config: Config = {
    content: ['./src/**/*.{ts,tsx}'],
    theme: {
      extend: {
        colors: {
          softwhite: '#F8FAFC',
          navy: '#0B1F3B',
          orange: '#FF7A00',
        },
        fontFamily: {
          sans: ['ui-sans-serif', 'system-ui', '-apple-system', 'Segoe UI', 'Roboto', 'Ubuntu', 'Cantarell', 'Noto Sans', 'sans-serif'],
        },
      },
    },
    plugins: [],
  }
  export default config
  ```

- **Global styles:** `src/app/globals.css`
  ```css
  @tailwind base;
  @tailwind components;
  @tailwind utilities;

  :root {
    color-scheme: light;
  }

  html, body {
    @apply bg-softwhite text-navy font-sans;
  }
  ```

- **Layout shell:** `src/app/layout.tsx`
  ```tsx
  export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
      <html lang="en">
        <body>
          <header className="px-4 py-3 flex items-center justify-between border-b border-navy/10">
            <div className="flex items-center gap-2">
              <div className="h-8 w-8 bg-orange rounded-md animate-[fadeIn_600ms_ease-out]"></div>
              <span className="font-semibold">Bluestron</span>
            </div>
            <nav className="hidden md:flex gap-4 text-sm">
              <a href="/" className="hover:text-orange">Home</a>
              <a href="/courses" className="hover:text-orange">Courses</a>
              <a href="/services" className="hover:text-orange">Services</a>
              <a href="/blog" className="hover:text-orange">Blog</a>
              <a href="/contact" className="hover:text-orange">Contact</a>
            </nav>
          </header>
          <main>{children}</main>
          <footer className="px-4 py-6 text-sm text-navy/70">© Bluestron</footer>
        </body>
      </html>
    )
  }
  ```

- **Stub homepage:** `src/app/page.tsx`
  ```tsx
  export default function HomePage() {
    return (
      <section className="px-4 py-10">
        <h1 className="text-2xl md:text-3xl font-bold">Professional Training, Research & Software Development in Data, M&E, Management</h1>
        <p className="mt-3 text-navy/70">Minimalistic, mobile-first, managerial design.</p>
        <div className="mt-6 flex gap-3">
          <a className="px-4 py-2 bg-orange text-white rounded-md" href="/courses">Register now</a>
          <a className="px-4 py-2 border border-navy/20 rounded-md" href="/services">Learn more</a>
        </div>
      </section>
    )
  }
  ```

- **Admin shell route (protected later):** `src/app/admin/page.tsx`
  ```tsx
  export default function AdminShell() {
    return (
      <section className="px-4 py-10">
        <h2 className="text-xl font-semibold">Admin</h2>
        <p className="text-navy/70">CMS will appear here in Module 2.</p>
      </section>
    )
  }
  ```

- **Consent banner stub:** `src/components/ConsentBanner.tsx`
  ```tsx
  'use client'
  import { useEffect, useState } from 'react'

  export default function ConsentBanner() {
    const [consented, setConsented] = useState<boolean>(false)

    useEffect(() => {
      const v = localStorage.getItem('bs-consent')
      setConsented(v === 'accepted')
    }, [])

    useEffect(() => {
      if (consented) {
        // Load GA here
      }
    }, [consented])

    if (consented) return null

    return (
      <div className="fixed bottom-0 left-0 right-0 bg-navy text-softwhite px-4 py-3 text-sm flex items-center justify-between">
        <span>We use analytics to improve your experience.</span>
        <button
          className="px-3 py-1 bg-orange text-white rounded-md"
          onClick={() => {
            localStorage.setItem('bs-consent', 'accepted')
            setConsented(true)
          }}
        >
          Accept
        </button>
      </div>
    )
  }
  ```

- **Include banner in layout:** add `<ConsentBanner />` before `</body>`.

---

### Step 6 — Makefile (root-level)

Create `Makefile` in `ops/` or root (choose one; here root):

```makefile
# Load env if present
-include ops/.env.local

.PHONY: setup db-init api-dev ui-dev migrate test typecheck

setup:
	@echo "==> Setup: install frontend deps"
	cd bsui && npm install
	@echo "==> Setup: verify Maven"
	cd bsapi && ./mvnw -v

db-init:
	@echo "==> Initialize Postgres (bsdb)"
	psql -U postgres -h localhost -f ops/db-init.sql

api-dev:
	@echo "==> Run bsapi (Spring Boot)"
	cd bsapi && ./mvnw spring-boot:run

ui-dev:
	@echo "==> Run bsui (Next.js)"
	cd bsui && npm run dev

migrate:
	@echo "==> Run Java migration runner (Module 1 will implement)"
	cd bsapi && ./mvnw -q -DskipTests exec:java -Dexec.mainClass="co.ke.bluestron.bsapi.migration.Runner"

test:
	@echo "==> Backend tests"
	cd bsapi && ./mvnw test
	@echo "==> Frontend tests"
	cd bsui && npm test || echo "No tests yet"

typecheck:
	@echo "==> Frontend typecheck"
	cd bsui && npm run typecheck
```

---

### Step 7 — Local verification

- **Initialize DB:**
  ```bash
  make db-init
  ```
- **Run backend:**
  ```bash
  make api-dev
  ```
  - Check:
    - `http://localhost:8080/actuator/health` → `{"status":"UP"}`
    - `http://localhost:8080/api/status` → `{"status":"ok"}`
    - `http://localhost:8080/swagger-ui/index.html` → OpenAPI UI (empty for now)
- **Run frontend:**
  ```bash
  make ui-dev
  ```
  - Check:
    - `http://localhost:3000/` → stub homepage
    - `http://localhost:3000/admin` → admin shell
    - Consent banner appears; GA gated

---

### Step 8 — What’s next (Module 1 preview)

- **Entities:** CourseCategory, Course, CourseInstance, Venue, Registration; ServiceOffering, ServiceEnquiry; BlogPost.
- **Migrations:** Implement Java-based migration runner in `bsapi`:
  - Versioned classes (e.g., `V1__init_training_schema.java`)
  - Idempotent operations
  - Rollback support
- **OpenAPI contracts:** CRUD + list/filter endpoints; generate TypeScript clients for `bsui`.
- **Repositories/services:** Pagination, sorting, validation, error envelopes.

---

### Final checks and preferences

- **Postgres user/password:** Confirm you’re okay with `bsapi_user` and local password via `BSAPI_DB_PASSWORD` in `ops/.env.local`.  
- **OpenAPI:** Keep Springdoc UI enabled for local dev?  
- **CI:** Do you want GitHub Actions now or after Module 1?

If you’re good with this, I’ll proceed to map **Module 1 schemas and OpenAPI endpoints**—explicit fields, constraints, and the migration runner structure—so we can lock the domain and start building the admin in Module 2 without drift.