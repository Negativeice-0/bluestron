5.Bluestron Training Business.pdf

PDF

You are tasked with building a stripped‑down but modular MVP for Bluestron, a training business.
Input: a single uploaded PDF containing Bluestron’s requirements, sitemap, and course list.

Requirements:
1. Language/Frameworks:
   - Backend: Java 21, Spring Boot 3.x, strict coding practices (records, sealed classes, immutability, DTO separation).
   - Database: PostgreSQL with explicit schema.sql and seed.sql.
   - Frontend: Next.js 14 (App Router, TypeScript, strict ESLint/Prettier).
   - Build tools: Maven for backend, npm for frontend.
   - No Docker. Local dev only.

2. Architecture:
   - Monorepo with `backend/`, `frontend/`, `database/`, and a root `Makefile`.
   - Backend modules: `auth`, `course`, `category`, `registration`, `servicepages`, `admin`.
   - Frontend pages: Home, About, Courses (listing + filters), Course detail (register form), Research, Data Analysis, Software Dev, Contact, Admin (CRUD UI).
   - Admin CRUD: full create/update/delete for categories, courses, registrations, service pages.
   - Local auth: bcrypt password hashing, JWT tokens, role‑based access (ADMIN vs USER).
   - Registration workflow: course detail → register form → confirmation → email stub.

3. Database:
   - schema.sql: normalized tables (users, categories, courses, registrations, service_pages).
   - seed.sql: minimal seed (admin user, sample category/course).
   - Makefile targets: `db-create`, `db-drop`, `db-schema`, `db-seed`, `db-reset`, `db-psql`.

4. Backend:
   - application.yml with environment variables for DB connection.
   - SecurityConfig with JWT filter, stateless sessions, CORS.
   - DTOs for all requests/responses (no entity leakage).
   - Controllers under `/api/auth`, `/api/courses`, `/api/registrations`, `/api/admin/**`.
   - Unit tests for services and controllers.
   - Makefile targets: `be-build`, `be-run`, `be-test`, `be-curl-auth`, `be-curl-courses`, `be-curl-register`.

5. Frontend:
   - Next.js App Router, TypeScript, strict linting.
   - Pages: `/courses` (listing), `/courses/[slug]` (detail + register), `/admin/*` (CRUD).
   - Fetch wrapper with JWT support.
   - Minimal UI (cards, forms, tables) with TailwindCSS.
   - Makefile targets: `fe-install`, `fe-dev`, `fe-build`, `fe-start`.

6. Best Practices:
   - Explicit imports, comments, and file paths.
   - No silent failures: throw exceptions with clear messages.
   - Validation on all inputs (Bean Validation in backend, Zod in frontend).
   - Separation of concerns: entities vs DTOs vs services vs controllers.
   - Reproducibility: every step verifiable via Makefile commands and curl tests.
   - Accessibility: alt tags, semantic HTML, responsive design.

Deliverables:
- Full monorepo scaffold with backend, frontend, database, and Makefile.
- Strict, modern Java 21 code with DTOs, records, and immutability.
- Next.js frontend with TypeScript and TailwindCSS.
- Postgres schema and seed files.
- Makefile with verbose targets for DB, backend, frontend, and curl verification.

Goal:
Generate a reproducible, modular MVP that meets Bluestron’s PDF requirements, guarantees basic functionality (auth, CRUD, registration), and leaves room for iteration.