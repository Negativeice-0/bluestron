# Bluestron Super‑Prompt
Here’s the final, complete super‑prompt you can reuse:

Build Bluestron as a monorepo with:

Backend (Spring Boot 3.5.9, Java 21, Maven):

Minimal dependencies: Spring Web, Spring Data JPA, Validation, PostgreSQL driver, JWT library.

Package root: co.ke.bluestron.

Database: Postgres with raw SQL migrations (IF NOT EXISTS) in backend/migrations/.

Makefile targets: db-migrate, db-seed, backend-run, verify-*.

Auth: JWT for users and admins. Users redirect to their registered course page; admins redirect to /admin.

Media handling: videos, images, docs stored locally (/uploads/*), metadata in media table, served via API endpoints.

Frontend (Next.js latest, TypeScript strict, Tailwind v4 defaults):

App Router pages under app/.

Strict TypeScript: no any or unknown.

Tailwind v4 defaults only (@import "tailwindcss"; in globals.css).

Colors: purple-500, blue-500, orange-500, white (plus slate neutrals).

Use next/image for all images (SEO + performance).

Use <video> for videos served from backend.

Use framer-motion for carousels and transitions.

SEO via Metadata in layout.tsx and per page.

Sitemap (implement exactly as in Bluestron document):

Home: hero banner, 3 highlights, featured courses carousel, quick links, testimonials, footer.

About Us: mission, vision, team, why choose us, locations.

Courses: landing with filters, category pages, course detail with inline registration form.

Research Services: baseline/midline/endline/custom consultancy, case studies, enquiry form.

Data Analysis & Management Services: cleaning, visualization, tools, deliverables, enquiry form.

Software Development Services: custom apps, integration, maintenance, portfolio, enquiry form.

Blog: articles list/detail.

Contact Us: form, address, phone, email, map, newsletter.

Registration: inline form on course detail, confirmation page, emails to participant/admin.

Admin: dashboard with JWT auth, manage categories, courses, registrations, services, blog, testimonials, media.

Verification:

Provide curl commands for every endpoint (list, filter, detail, create).

Provide UI steps for each block.

Build dependent blocks one at a time (Categories → Courses → Registration → Services → Blog → Contact → Admin).

Verify each before moving forward.

Style:

Full file paths.

Full code for each file.

Brief allegorical comments explaining intent.

No Docker/Flyway/Jenkins.

Sequence:

Block 1: Categories + Home.

Block 2: Courses (with featured flag, inline registration).

Block 3: Registration (JWT issuance, redirect).

Block 4: Services pages.

Block 5: Blog.

Block 6: Contact + Newsletter.

Block 7: Admin (full CRUD, media library, video serving).