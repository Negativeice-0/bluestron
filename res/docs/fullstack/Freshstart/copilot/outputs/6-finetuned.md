# Detailed Prompt for Bluestron Super‑Prompt Generation

You are to generate a super‑prompt that will serve as the master plan for building Bluestron’s website and platform.

Constraints:

Backend: Spring Boot 3.5.9, Java 21, Maven, Postgres. Minimal dependencies (Web, JPA, Validation, PostgreSQL driver, JWT).

Frontend: Next.js  latest, strict TypeScript, Tailwind v4 defaults, next/image, framer-motion.

Auth: JWT for users and admins. Inline registration form on course detail pages.

Branding: Tailwind defaults (purple-500, blue-500, orange-500, white).

Database: Single source of truth for all content (courses, categories, registrations, services, blog, testimonials, media including videos).

Media: Videos stored locally, metadata in DB, served via backend.

No Docker, Flyway, Jenkins. Use raw SQL migrations + Makefile.

Requirements:

Follow Bluestron’s sitemap verbatim (Home, About, Courses, Research, Data, Software, Blog, Contact, Registration, Admin).

Map each requirement from the document to implementation details (tables, endpoints, frontend pages).

Provide elevator pitch explaining how the system works.

Juxtapose Bluestron’s requirements with implementation in a table.

Sequence build into dependent blocks (Categories → Courses → Registration → Services → Blog → Contact → Admin).

For each block: full file paths, full code, allegorical comments, Makefile targets, curl verification, UI steps.

Style:

Clear, complete, reproducible.

Allegorical comments (brief metaphors to explain intent).

Verification loops after each block.

No hidden magic, no skipped steps.

Output:

A super‑prompt that can be reused to generate each block.

Elevator pitch + juxtaposition table.

Explicit mention of JWT, inline registration, next/image, Tailwind v4 colors, framer‑motion.

Database as source of truth for all content including videos.