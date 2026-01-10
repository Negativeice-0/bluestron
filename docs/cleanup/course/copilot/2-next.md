No docker -- too complex for now, bt keep make file, no flyway we shall be using a java based migration runner which is just as good and we shall keep it local so as to avoid complexity. RegEmails -- googleapi, templates branded html. I already have paystack page -- just need to intergrate it so it is seamless. Admin mnaged markdown posts. case study can wait. Yes on cohorts and structured venue. No contraints on brand assests, a nice system ui would be nice to easen the process. A consent message with an accept button would be nice.
















































### Updated scope lock

Got it—no Docker, keep Makefile; local Java-based migration runner (no Flyway); Gmail API with branded HTML templates; Paystack integration for seamless payments; admin-managed markdown blog; cohorts with structured venues; case studies later; system UI typography; consent message with accept button. I’ll reflect all of that in the build and sequencing.

---

### Architecture and module order (finalized)

#### Module order

- **Module 0:** System skeleton (backend, frontend, migration runner wiring, CI, Makefile).  
- **Module 1:** Core domain (entities, migrations, OpenAPI contracts, repositories/services).  
- **Module 2:** Admin capabilities (CRUD for all entities, role-based access, markdown blog).  
- **Module 3:** Public read mode (pages, registration flow, Paystack handoff, Gmail API emails).  
- **Module 4:** Composition (filters/search/SEO/analytics/consent).

#### Bounded contexts

- **Training:** CourseCategory, Course, CourseInstance (cohorts), Registration.  
- **Research/Data Analysis/Software Development:** ServiceOffering, ServiceEnquiry.  
- **Blog:** Markdown posts (admin-managed).  
- **Payments:** Paystack integration (frontend handoff + backend verification).  
- **Notifications:** Gmail API transactional emails (HTML templates).

---

### Design system and UX (mobile-first, managerial)

- **Palette:** Soft white (#F8FAFC), navy blue (#0B1F3B), orange accent (#FF7A00).  
- **Typography:** System UI stack (e.g., `ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto, ...`).  
- **Components:**  
  - **Hero:** value proposition + CTAs (“Register now”, “Enquire”, “Learn more”).  
  - **Course tiles:** thumbnail, title, short descriptor, category chip, “Register” button.  
  - **Filters:** mobile drawer; desktop sidebar; instant apply.  
  - **Forms:** single-column, inline validation, accessible labels, success states.  
  - **Consent banner:** minimal bar with “Accept” button; GA loads post-consent.  
  - **Logo animation:** subtle fade/scale; respects reduced motion.

---

### Tech stack standards (no drift)

#### Backend (Spring Boot, Java 21)

- **Structure:** `controller` → `service` → `repository` → `entity`, plus `dto`, `mapper`, `config`.  
- **Contracts:** OpenAPI-first; strict request/response validation; error envelopes.  
- **Persistence:** Postgres; **Java-based migration runner** (local, versioned, idempotent); rollback scripts.  
- **Security:** JWT/OAuth2 for admin; CSRF for admin; CORS policy; input sanitization.  
- **Notifications:** Gmail API client; branded HTML templates; retry/backoff; audit logs.  
- **Payments:** Paystack callback verification endpoint; signed payload validation; status updates on Registration.  
- **Observability:** Actuator, structured logs, request IDs, health/readiness.

#### Frontend (Next.js App Router + TypeScript)

- **Standards:** TypeScript strict; ESLint/Prettier; Tailwind CSS with tokens; accessibility-first.  
- **Data:** Generated TypeScript clients from OpenAPI; SWR/React Query; error boundaries; skeleton loaders.  
- **Admin:** Markdown editor for blog posts; CRUD pages for all entities; role-based route guards.  
- **Public:** Clean slugs; dynamic routes for categories/courses; registration forms; Paystack handoff; consent banner gating analytics.  
- **Email templates:** Branded HTML stored as versioned assets; preview in admin.

#### Dev workflows

- **Makefile:** `make setup`, `make dev`, `make test`, `make migrate`, `make seed`, `make typecheck`.  
- **Envs:** `.env.example` with explicit keys; local-only secrets; feature flags for blog/payments.  
- **CI:** Lint/test/build; OpenAPI contract validation; schema diff checks; e2e smoke.

---

### Domain model (key entities)

- **CourseCategory:** `id`, `slug`, `name`, `description`, `status`.  
- **Course:** `id`, `slug`, `title`, `shortDescription`, `fullDescription`, `learningOutcomes[]`, `thumbnailUrl`, `categoryId`, `status`.  
- **CourseInstance (cohort):** `id`, `courseId`, `mode` (in-person/online), `startDate`, `endDate`, `capacity`, `status`, `venueId`.  
- **Venue (structured):** `id`, `name`, `city`, `address`, `mapUrl`, `notes`.  
- **Registration:** `id`, `courseId`, `courseInstanceId`, `fullName`, `email`, `phone`, `organization`, `role`, `preferredDate`, `paymentOption` (Online/Invoice), `paymentStatus`, `createdAt`.  
- **ServiceOffering:** `id`, `slug`, `title`, `description`, `status`, `category` (research/data/software).  
- **ServiceEnquiry:** `id`, `serviceOfferingId`, `fullName`, `email`, `phone`, `organization`, `message`, `createdAt`.  
- **BlogPost:** `id`, `slug`, `title`, `summary`, `markdown`, `thumbnailUrl`, `status`, `publishedAt`.

---

### Registration and payments flow (seamless)

- **Course detail:** prominent “Register” button.  
- **Form fields:** pre-populated course; cohort selection; personal/org details; payment option (Online/Invoice).  
- **If Online:**  
  - **Frontend:** redirect to Paystack page with signed payload; show pending state.  
  - **Backend:** verify callback; update `paymentStatus` (success/failed); persist transaction reference; emit confirmation email.  
- **Emails (Gmail API):**  
  - **Participant:** branded HTML with course/cohort details, payment status, next steps.  
  - **Admin:** participant details, course/cohort, payment status, quick links.  
- **Confirmation:** thank-you page with status; retry link if payment failed.

---

### Verification checkpoints

- **Module 0:**  
  - **Backend:** `/actuator/health` UP; OpenAPI served; Postgres connects; migration runner wired (no migrations yet).  
  - **Frontend:** Home renders; admin shell route protected; typecheck clean; lint/test pass.  
  - **Makefile:** all commands run locally without Docker.

- **Module 1:**  
  - **Migrations:** create schemas/tables; idempotent; rollback tested.  
  - **Contracts:** OpenAPI endpoints for CRUD/list/filter; generated TS clients; e2e smoke.  
  - **Repositories/services:** pagination/sorting; validation; error envelopes.

- **Module 2:**  
  - **Admin CRUD:** all entities editable; markdown blog; audit logs; role-based access.  
  - **Email preview:** templates render; test send via Gmail API (local).

- **Module 3:**  
  - **Public pages:** sitemap complete; registration flow; Paystack handoff; confirmation screens.  
  - **Enquiry forms:** service pages submit; admin receives notifications.

- **Module 4:**  
  - **Filters/search:** category/mode/date; clean URLs; breadcrumbs; meta tags; GA gated by consent.  
  - **Accessibility:** labels, contrast, keyboard nav; reduced motion respected.

---

### Two final clarifications

- **Paystack integration:** Do you already have the public keys and callback URL you want us to use for local dev, or should we stub the callback and keep keys in `.env.local`?  
- **Gmail API:** Should we use OAuth client credentials for local dev or service account with domain-wide delegation (if available)?

---

### Next step

If the above scope and flows are approved, I’ll proceed with **Module 0** exactly as specified—Makefile-driven local setup, Spring Boot + Next.js skeletons, migration runner wiring, OpenAPI baseline, type-safe frontend shell, and CI scaffolding—so we can verify health, contracts, and reproducibility before moving into the domain.

