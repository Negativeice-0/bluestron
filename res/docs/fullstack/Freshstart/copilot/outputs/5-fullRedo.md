## Bluestron super‑prompt (Java 21, Spring Boot 3.5.9, Maven, Next.js TS, Tailwind v4 defaults, Postgres)

Use this prompt verbatim to generate each dependent block (Categories → Courses → Registration → Services → Blog → Contact → Admin). It encodes your constraints, sitemap, branding, and verification loops so we can build cleanly, stack wins, and avoid drift.

---

### Monorepo and core constraints

- **Monorepo:** `bluestron/` with `backend/` (Spring Boot) and `frontend/` (Next.js).
- **Java:** 21 (no legacy patterns).
- **Spring Boot:** 3.5.9, **Maven**, minimal dependencies (Web, Data JPA, Validation, PostgreSQL driver).
- **Database:** Postgres with raw SQL migrations (`IF NOT EXISTS`) executed via `Makefile`—no Flyway/Docker/Jenkins.
- **Frontend:** Next.js latest, TypeScript strict, App Router, Tailwind v4 defaults (no config files), **use `<img>`** (not `next/image`), **framer‑motion** for smooth UI without brittle code.
- **Branding:** Bluestron palette (deep blue `#0A4D8C`, bright blue `#1E88E5`, cyan `#00C2FF`, dark `#062B4F`, light `#E6F2FA`), clean modern layout.
- **TypeScript:** Strict—no `any` or `unknown` in signatures; explicit types everywhere.
- **Verification:** Every block ships with `curl` commands and UI checks before proceeding.
- **Admin:** Minimal guard via `X-Admin-Token` header (env‑based), full CRUD pages under `/admin/*`, expandable later to proper auth.

---

### Backend configuration (use exactly)

- **Datasource:**  
  `jdbc:postgresql://localhost:5432/bsdb`  
  `username: bsdbu`  
  `password: bsdbp2Pass&1!`
- **Spring settings:** `ddl-auto: none`, `open-in-view: false`, UTC time zone, health/mappings/info endpoints exposed.
- **Package root:** `co.ke.bluestron`.

---

### Sitemap (implement religiously)

1. **Home**
   - Hero banner with value proposition.
   - Three highlights: **Training** (lead), **Research Services**, **Software Development**.
   - **Featured/upcoming courses carousel** (framer‑motion).
   - Quick links to course categories.
   - Testimonials/Success stories.
   - Footer with contact info, social links, newsletter sign‑up.

2. **About Us**
   - Mission, vision, values.
   - Team/trainers.
   - Why choose us (experience, certifications, results).
   - Locations/service area.

3. **Courses / Training**
   - Landing page with filters: **Category, Duration, Mode, Date**, search box.
   - Category pages: list of courses (thumbnail, title, short description, **Register** button).
   - Course detail page: full description, learning outcomes, duration, dates, **registration form**, pricing or “contact us”.

4. **Research Services**
   - Baseline, Midline, Endline, Custom Consultancy.
   - Case studies/sample projects.
   - Enquiry form.

5. **Data Analysis & Management Services**
   - Data cleaning/processing/analysis.
   - Visualization/dashboards.
   - Software tools/custom analytics.
   - Example deliverables.
   - Enquiry form.

6. **Software Development Services**
   - Custom web/mobile for data collection, dashboards, apps.
   - Integration & automation.
   - Maintenance & support.
   - Portfolio/examples.
   - Enquiry form.

7. **Blog / Insights**
   - Articles on training, research methodology, data analytics, software trends.

8. **Contact Us**
   - Contact form.
   - Address, phone, email.
   - Map/location info.
   - Newsletter sign‑up.

9. **Registration / Enrolment / Payment**
   - Integrated on course pages.
   - Workflow: select course → form (Full Name, Email, Phone, Organization, Role, Course Selected pre‑populated, Preferred Date/Venue, Payment Option: Online/Invoice me) → confirmation page → emails to participant and admin.

10. **Admin**
   - Dashboard: manage Categories, Courses, Registrations, Services, Blog posts.
   - CRUD endpoints under `/api/admin/*` guarded by `X-Admin-Token`.
   - Admin pages under `/admin/*` with forms/tables.

---

### Block sequencing (dependent blocks)

1. **Block 1: Categories**
   - DB: `bluestron.categories` table (`id`, `slug` unique, `name`, `description`, timestamps), seed with 5 core categories from the document.
   - Backend: Entity, Repository, DTOs, Service, Controller (`/api/categories` list/get/post).
   - Frontend:
     - Home: hero, three highlights, **featured carousel** (framer‑motion), categories grid.
     - Courses landing: sidebar categories, placeholder for courses (to be filled in Block 2).
   - Verification:
     - `make db-migrate && make db-seed`
     - `mvn spring-boot:run`
     - `curl http://localhost:8080/api/categories`
     - UI: Home shows carousel + categories; Courses page shows filters and selected category.

2. **Block 2: Courses (depends on Categories)**
   - DB: `bluestron.courses` table with FK to `categories(id)`, fields: `slug` unique, `title`, `short_desc`, `full_desc`, `duration`, `mode`, `start_date`, `end_date`, `location`, `thumbnail_url`, `price`, `featured` boolean; seed with the full list from the document grouped by category.
   - Backend: Entity, Repository, DTOs, Service, Controllers:
     - Public: `/api/courses`, `/api/courses?category=slug`, `/api/courses/{slug}`
     - Admin: `/api/admin/courses` (create/update/delete)
   - Frontend:
     - Courses landing: filters (Category, Duration, Mode, Date), search; tiles with thumbnail, title, short description, **Register** button.
     - Course detail: full description, outcomes, dates, **inline registration form** (not modal unless requested).
     - Home carousel pulls `featured=true` courses.
   - Verification:
     - `curl` list/filter/detail.
     - UI: category filter shows correct courses; detail page renders form.

3. **Block 3: Registration**
   - DB: `bluestron.registrations` table: participant fields + course FK + status.
   - Backend: `/api/registrations` (create), `/api/admin/registrations` (list/manage).
   - Email: stub service (log only) to avoid external deps; later swap to SMTP/API.
   - Frontend: submit form → thank‑you page → admin list.

4. **Block 4: Services (Research, Data, Software)**
   - Static content + enquiry forms.
   - Backend: `/api/enquiries` (create), `/api/admin/enquiries` (list).
   - Frontend: pages per sitemap.

5. **Block 5: Blog**
   - DB: `bluestron.posts` (slug, title, content, tags, published_at).
   - Backend: public list/get; admin CRUD.
   - Frontend: list/detail.

6. **Block 6: Contact & Newsletter**
   - Contact form → `/api/contact`.
   - Newsletter stub (store emails in `bluestron.newsletter_subscribers`).

---

### Code generation requirements (per block)

- **Backend (Maven):**
  - `pom.xml` with Spring Boot 3.5.9, Java 21, minimal deps.
  - `application.yml` exactly as provided.
  - Entities with timestamps (`OffsetDateTime`), no Lombok.
  - Controllers return DTOs; validation via `jakarta.validation`.
  - Admin guard via `X-Admin-Token` header (env `BS_ADMIN_TOKEN`).
- **DB migrations:**
  - Raw SQL files in `backend/migrations/` with `IF NOT EXISTS`.
  - `Makefile` targets: `db-migrate`, `db-seed`, `backend-run`, `verify-*`.
- **Frontend (Next.js, TS, Tailwind v4 defaults):**
  - Strict TS: explicit types, no `any`/`unknown`.
  - App Router pages under `app/`.
  - Tailwind v4 defaults only (`@import "tailwindcss";` in `globals.css`).
  - **Use `<img>`** tags (not `next/image`).
  - **framer‑motion** for carousel and subtle transitions.
  - SEO via `Metadata` in `layout.tsx` and per page.
  - Bluestron palette applied via inline hex colors in classes (no config files).
- **UI components:**
  - Featured carousel (`framer‑motion`), category badges, course tiles, forms.
  - Accessibility: alt text, labels, readable contrast.

---

### Verification and reproducibility

- Provide `curl` commands for every endpoint (list, filter, detail, create).
- Provide UI steps to confirm each block.
- Keep comments allegorical but brief—explain intent without noise.
- No external services (email/logging stubs only).
- No Docker/Flyway/Jenkins.

---

### Deliverables format

- Full file paths.
- Full code for each file.
- Makefile targets.
- Short comments summarizing how the code works.
- A final “Verification” section per block.

---

### Start now with Block 1 (Categories) and Home page

- Generate:
  - `backend/pom.xml`, `backend/src/main/resources/application.yml`, `BluestronApplication.java`.
  - `backend/migrations/001_init_schema.sql`, `002_categories_table.sql`, `003_seed_categories.sql`.
  - `backend/src/main/java/co/ke/bluestron/categories/*` (Entity, Repository, DTOs, Service, Controller, AdminController).
  - `Makefile` with `db-migrate`, `db-seed`, `backend-run`, `verify-categories`.
  - `frontend/package.json`, `tsconfig.json`, `next.config.ts` (empty), `app/layout.tsx`, `app/globals.css`.
  - `frontend/lib/api.ts` (typed fetchers).
  - `frontend/components/FeaturedCarousel.tsx` (framer‑motion, `<img>`).
  - `frontend/app/page.tsx` (hero, three highlights, featured carousel, categories grid).
  - `frontend/app/courses/page.tsx` (filters scaffold).
  - `frontend/app/admin/page.tsx`, `frontend/app/admin/categories/page.tsx` (create category).

- Include verification:
  - `make db-migrate && make db-seed`
  - `mvn spring-boot:run`
  - `curl http://localhost:8080/api/categories`
  - UI checks for Home and Courses.

---

### One preference to confirm

- Registration form placement: **inline on course detail page** (default), or **modal**?  
  If inline is fine, proceed with inline in Block 2.

---

You know what? This is exactly the kind of disciplined build you thrive on—clean contracts, visible progress, no hidden magic. If anything in this prompt feels off or too opinionated, say it now and we’ll tune it before we generate Block 1 code.