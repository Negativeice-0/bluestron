proceed

Perfect—let’s lock in **Module 0** and start the Bluestron ecosystem build exactly as scoped. This is the foundation layer, no business logic yet, just the skeleton that guarantees reproducibility and prevents drift.

---

## 🔹 Module 0 — System Skeleton

### Backend (Spring Boot 3.x, Java 21)
- **Project setup:**  
  - `bluestron-backend/` with Gradle/Maven build.  
  - Packages: `controller`, `service`, `repository`, `entity`, `dto`, `config`.  
- **Postgres connection:**  
  - Local-only, configured via `.env.local`.  
  - Migration runner wired (Java-based, not Flyway).  
- **Health endpoints:**  
  - `/actuator/health` → returns `UP`.  
  - `/api/status` → simple JSON `{ "status": "ok" }`.  
- **Contracts:**  
  - Empty OpenAPI spec served at `/api/docs`.  
  - Ready for domain entities in Module 1.  
- **Security stub:**  
  - Basic JWT filter wired, no roles yet.  
- **Makefile commands:**  
  - `make setup` → install deps, init DB.  
  - `make dev` → run backend locally.  
  - `make migrate` → run Java migration runner.  
  - `make test` → run unit tests.  
  - `make typecheck` → verify contracts.

---

### Frontend (Next.js App Router + TypeScript)
- **Project setup:**  
  - `bluestron-frontend/` with strict TypeScript, ESLint, Prettier.  
  - Tailwind CSS with design tokens (soft white, navy blue, orange).  
- **Layout shell:**  
  - Header/footer placeholders.  
  - Stub homepage route `/`.  
  - Admin shell route `/admin` (protected, no entities yet).  
- **Routing:**  
  - App Router structure with `layout.tsx`, `page.tsx`.  
  - Clean slugs ready for categories/courses later.  
- **CI hooks:**  
  - Lint/test/build checks.  
  - Typecheck enforced.  
- **Consent banner stub:**  
  - Minimal bar with “Accept” button.  
  - GA loads only after consent.

---

### Shared (Cross-cutting)
- **Environment:**  
  - `.env.example` with explicit keys (DB, Gmail API, Paystack).  
- **CI/CD:**  
  - GitHub Actions or equivalent: lint/test/build pipeline.  
- **Telemetry hooks:**  
  - Structured logs, request IDs, error envelopes.  
- **Brand assets:**  
  - Logo placeholder with subtle animation (fade/scale).  
  - System UI typography stack.

---

## ✅ Verification checklist for Module 0
- Backend boots, connects to Postgres, `/actuator/health` returns UP.  
- OpenAPI spec served (empty).  
- Migration runner wired (no migrations yet).  
- Frontend boots, renders stub homepage and admin shell.  
- Typecheck/lint/test all pass.  
- Makefile commands run cleanly.  
- Consent banner visible, GA gated.

---

### Next step
Once Module 0 is confirmed working locally, we’ll move to **Module 1 (Core Domain)**: defining entities (CourseCategory, Course, CourseInstance, Registration, ServiceOffering, ServiceEnquiry, BlogPost), migrations, repositories, and OpenAPI contracts.  

Would you like me to **map out the exact schema definitions and OpenAPI endpoints for Module 1** now so you can see how the domain will be locked before we scaffold?