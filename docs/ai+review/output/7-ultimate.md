# Bluestron master control prompt (module 0 + module 1 categories end‑to‑end)

Use this prompt verbatim to generate, verify, and stress‑test Bluestron according to the attached PDF. It enforces ordering, short names, best practices, and complete deliverables—no drift, no half measures.

---

## Canonical constraints

- **Stack:** Spring Boot (Maven, Java 21, `application.yaml`), Next.js (TypeScript, App Router, Tailwind), Postgres (local), Java‑based migration runner (no Flyway), Makefile (no Docker).
- **Names:** `bsapi` (backend), `bsui` (frontend), `bsdb` (database).
- **Branding:** minimalistic, mobile‑first, managerial style—soft white, navy blue, orange.
- **Security:** Spring Security minimal for Module 1 (permit public endpoints), password auth added in Module 2.
- **Content:** Admin‑managed dynamic content; schema changes via migrations only.
- **Verification:** Every module must include curl, UI, and DB checks.

---

## Canonical module order

1. **Module 0—System skeleton:** Projects, configs, Makefile, DB roles, health endpoints.
2. **Module 1—Categories end‑to‑end:** Migrations, entity, DTO, repository, service, controller, curl, UI page, tests, and stress test (100 simulated users).
3. **Module 2—Auth + admin API:** JWT, roles, secure CRUD, audit propagation.
4. **Module 3—Admin UI:** Full CMS for all entities.
5. **Module 4—Public UI:** Homepage composites, courses, services, blog, registration flow.
6. **Module 5—Payments & notifications:** Paystack integration, Gmail API HTML templates.

Do not jump ahead. Complete Module 0, then Module 1 categories end‑to‑end with verification and stress test before proceeding.

---

## Deliverables for module 0

- **Backend:** `bsapi` Spring Boot app with:
  - `BsapiApplication.java` in `co.ke.bluestron.bsapi`
  - `config/SecurityConfig.java` (CSRF disabled, permit `/api/**`, `/actuator/**`, `/swagger-ui/**`)
  - `config/CorsConfig.java` (allow `http://localhost:3000`)
  - `controller/StatusController.java` (`GET /api/status`)
  - `migration/` scaffolding (`Migration`, `Runner`)
  - `application.yaml` with Postgres config and JPA settings
- **Frontend:** `bsui` Next.js app with:
  - Tailwind tokens: `softwhite`, `navy`, `orange`
  - `components/Navbar.tsx`, `components/Footer.tsx`
  - `app/layout.tsx` using Navbar/Footer
  - `app/page.tsx` stub hero with CTA buttons
- **Ops:** `Makefile` with `setup`, `db-init`, `api-dev`, `ui-dev`, `migrate`, `typecheck`
- **DB:** `ops/db-init.sql` creating `bsdb`, role `bsapi_user`, schemas, and default privileges (no readonly role)

**Verification:**
- `make db-init`
- `make api-dev` → `GET /actuator/health`, `GET /api/status`, `Swagger UI`
- `make ui-dev` → homepage renders

---

## Deliverables for module 1 (categories end‑to‑end)

Implement the following structure under `co.ke.bluestron.bsapi`:

- `entity/CourseCategory.java` — JPA entity with audit fields
- `dto/CategoryDTO.java` — validated DTO
- `repository/CourseCategoryRepository.java` — JPA repo with `findBySlug`, `existsBySlug`
- `service/CategoryService.java` — business logic (create, list, get, update, delete)
- `controller/CategoryController.java` — REST endpoints with OpenAPI annotations:
  - `GET /api/categories`
  - `POST /api/categories`
  - `GET /api/categories/{slug}`
  - `PUT /api/categories/{slug}`
  - `DELETE /api/categories/{slug}`
- `migration/V1__InitTrainingSchema.java` — DDL for `course_category` with indexes and audit fields
- `application.yaml` — ensure `ddl-auto: none`, `open-in-view: false`, actuator `mappings` enabled

**Frontend page:**
- `bsui/src/app/categories/page.tsx` — fetch and render categories with cards (mobile‑first, soft white/navy/orange), error handling, and Navbar/Footer.

**Curl verification:**
- Create:
  ```bash
  curl -X POST http://localhost:8080/api/categories \
    -H "Content-Type: application/json" \
    -d '{"slug":"monitoring-evaluation","name":"Monitoring & Evaluation","description":"M&E courses","status":"active"}'
  ```
- List:
  ```bash
  curl http://localhost:8080/api/categories
  ```
- Get:
  ```bash
  curl http://localhost:8080/api/categories/monitoring-evaluation
  ```
- Update:
  ```bash
  curl -X PUT http://localhost:8080/api/categories/monitoring-evaluation \
    -H "Content-Type: application/json" \
    -d '{"slug":"monitoring-evaluation","name":"M&E","description":"Updated","status":"active"}'
  ```
- Delete:
  ```bash
  curl -X DELETE http://localhost:8080/api/categories/monitoring-evaluation
  ```

**DB verification:**
```bash
psql -U bsapi_user -d bsdb -c "SELECT id, slug, name, status FROM course_category;"
```

**Backend tests:**
- `@SpringBootTest` + `@AutoConfigureMockMvc` for controller create/list/get/update/delete
- `@DataJpaTest` for repository lookups

**Frontend tests:**
- React Testing Library: mock `fetch`, assert card rendering and error state

---

## Stress test (simulate 100 users)

Add a simple load simulator to `bsapi`:

- `testload/CategoryLoadSimulator.java` — a Spring `@Component` or standalone `main` that:
  - Creates 5 categories (if not exist)
  - Spawns 100 threads (or uses `ExecutorService`) to:
    - Randomly list categories (`GET /api/categories`)
    - Randomly create/update/delete categories (if allowed in Module 1)
  - Logs success/error counts

Run via:
```bash
cd bsapi && ./mvnw -q -DskipTests exec:java -Dexec.mainClass="co.ke.bluestron.bsapi.testload.CategoryLoadSimulator"
```

Or wire as a `@Bean` gated by `APP_LOAD_TEST=true` env var to avoid accidental runs.

---

## Rules for generation

- **No omissions:** Provide complete, compilable source files for every listed class and page.
- **Package scanning:** Ensure `BsapiApplication` is in `co.ke.bluestron.bsapi` so all subpackages are scanned.
- **HTTP status codes:** `201` for create, `200` for list/get/update, `204` for delete, `400` for validation errors.
- **Audit fields:** Set `created_by`, `updated_by` to `"system"` in Module 1; switch to authenticated user in Module 2.
- **Best practices:** 
  - Backend: DTO validation, service transactions, repository queries, OpenAPI annotations.
  - Frontend: `next/link`, error handling, mobile‑first Tailwind, no inline styles, clean components.
- **Verification first:** After generation, include explicit curl commands, DB queries, and UI checks.

---

## After module 1 success

Only after categories pass curl/UI/DB/stress tests, proceed to Module 2 (Auth + admin API) with:
- JWT login, roles (`ADMIN`, `EDITOR`, `VIEWER`)
- Secure admin endpoints under `/api/admin/categories`
- Audit propagation from principal
- Updated curl with `Authorization: Bearer <token>`

---

## Final instruction

Generate Module 0 and Module 1 exactly as specified above, with full source code, Makefile targets, curl scripts, DB verification commands, and a 100‑user stress test runner. Do not proceed to Module 2 until all verifications pass.