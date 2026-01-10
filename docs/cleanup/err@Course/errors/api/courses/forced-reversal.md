Due to too many errors:

Here’s a **foolproof master prompt** you can use to start fresh and rebuild Bluestron strictly according to the attached requirements, with sequential progression through modules, clean backend, strict DTOs, and a Next.js frontend styled in **navy blue, orange, and softwhite using regular Tailwind (no custom configs)**.

---

## 🎯 Master Prompt for Bluestron Rebuild

```
You are to act as a full-stack architect and developer. Start fresh and build Bluestron strictly according to the attached requirements (Bluestron Training Business.pdf). Follow a sequential modular progression:

### Module 0 (System Skeleton)
- ✅ Already complete: backend project scaffold, frontend project scaffold, configs, DB roles, Makefile, health endpoints.
- Do not redo this; assume it exists.

### Module 1 (Core Domain)
- Categories: ✅ End-to-end working (migration, entity, DTO, repo, service, controller, curl, UI).
- Courses: ⚠️ Must be rebuilt cleanly with slug included, strict DTOs, repository/service/controller separation, curl-first verification, and UI detail page.
- Remaining entities to scaffold in this module:
  - CourseInstances (cohorts with dates, mode, venue, capacity)
  - Registrations (form fields: name, email, phone, organization, role, payment option)
  - Trainers (bio, expertise, photo)
  - Testimonials (quote, name, organization)
  - FeaturedContent (highlighted courses/services)
  - Blog (posts with title, body, author, date)
  - Services (Research, Data Analysis, Software Development)

**Backend rules:**
- Use migrations (`V1__InitTrainingSchema`, `V2__InitCourseSchema`, etc.) for schema evolution.
- Entities must not leak directly; always return DTOs.
- Service layer initializes collections before mapping to DTOs (no lazy-loading errors).
- Controllers expose clean REST endpoints (`/api/categories`, `/api/courses`, etc.).
- Global exception handler returns 400/404/409/500 with clean JSON messages.
- SecurityConfig: disable CSRF, permit all `/api/**` until Module 2.

**Frontend rules:**
- Next.js + strict TypeScript.
- TailwindCSS with Bluestron palette: navy blue (#001f3f), orange (#ff851b), softwhite (#f8f9fa).
- No custom Tailwind configs—use default classes.
- Pages:
  - `/courses`: list view with filters (category, mode, date).
  - `/courses/[slug]`: detail view with description, outcomes, content blocks, register button.
  - `/services`: separate pages for Research, Data Analysis, Software Development.
  - `/about`, `/contact`, `/blog`.
- Components: Navbar, Footer, CourseCard, RegisterForm.
- Responsive design (mobile/tablet/desktop).
- Clear CTAs: “Register Now”, “Enquire”, “Learn More”.

### Module 2 (Auth/Admin API)
- JWT-based authentication.
- Admin endpoints for managing categories, courses, content, registrations, blog posts, services.

### Module 3 (Admin UI)
- Next.js admin dashboard.
- CRUD for categories, courses, trainers, testimonials, blog posts, services.
- Registration management.

### Module 4 (Public UI Composites + SEO)
- Filtering/search on courses page.
- Featured courses carousel.
- Testimonials section.
- SEO-friendly URLs, meta tags, alt images.

### Module 5 (Payments & Notifications)
- Payment integration (offline invoice option first, online later).
- Email notifications for registration confirmation (to participant and admin).
- Thank-you page after registration.

---

### WhatsApp Analogy (for executive clarity)
- Courses = Chats.
- Content blocks (videos, quizzes, notes, files) = Messages.
- Categories = Groups.
- Registrations = Sending a message (form submission).
- Migrations = App updates (new features).
- Admin UI = WhatsApp Business (manage content, not schema).
- DTOs = Getters (UI display).
- Services = Setters (actions like send, register).

---

### Deliverables per module
- Backend: migrations, entities, DTOs, repos, services, controllers, curl commands for verification.
- Frontend: Next.js pages, strict TS components, Tailwind styling with navy/orange/softwhite.
- Documentation: endpoint list, curl examples, UI screenshots.

---

### Sequential progression
- Do not jump ahead. Finish each module end-to-end (backend + curl + frontend UI) before moving to the next.
- Always verify with curl before wiring UI.
- Always return DTOs, never JPA entities.
- Always style UI with navy/orange/softwhite using default Tailwind classes.

```

---

## 🚀 How to use this prompt
- Paste it into your dev assistant or planning tool.
- It will guide you step by step, ensuring you don’t get stuck mid‑way.
- Each module builds on the last, with curl-first verification before UI wiring.
- The WhatsApp analogy keeps executive explanations simple.

---

Would you like me to **expand Module 1 into exact Java + Next.js file scaffolds** (entities, DTOs, controllers, pages) so you can paste them directly and run `make api-dev` + `make ui-dev` without ambiguity?