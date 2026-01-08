To generate the absolute full-stack source code, environment configurations, and automation scripts you’ve seen so far in one go, a prompt must be **architecturally prescriptive**. It needs to treat the AI not just as a coder, but as a **Platform Engineer** and **Systems Architect**.

Here is the exhaustive "Senior-Architect" Super Prompt. This prompt is designed to trigger the generation of a complete, production-ready ecosystem from a single command.

---

### The Bluestron "Platform Engine" Super Prompt

> **Role:** Act as a Senior Full-Stack Platform Engineer and Systems Architect.
> **Objective:** Initialize and build a complete, production-ready ecosystem for **Bluestron**, a training and research business. The project must be built from zero (`mkdir bluestron`) and be fully containerized, type-safe, and automated.
> **1. Project Scaffolding & Initialization:**
> * Provide the exact terminal commands to initialize the workspace: `mkdir bluestron`, `mkdir api ui ops`.
> * **API (Backend):** Provide the configuration for a **Spring Boot 4.0.1 (Java 21)** project (Maven). Include dependencies: Web, JPA, Postgres, Validation, Security, and Lombok.
> * **UI (Frontend):** Provide the `npx create-next-app@latest` configuration (TypeScript, Tailwind, App Router).
> 
> 
> **2. Infrastructure & Orchestration (ops):**
> * **Docker:** Create a `docker-compose.yml` in `/ops` to orchestrate a PostgreSQL container (named `bsapi`), the Spring Boot `api`, and the Next.js `ui`.
> * **Networking:** Ensure all services communicate over a private `bluestron-network`.
> * **Automation:** Provide a root-level **Makefile** with targets for `setup`, `up` (builds and starts all), `down`, `db-shell`, and `seed`.
> * **Java Environment:** Include instructions for running `mvn clean install` and specific VS Code settings (`settings.json`) to ensure the Language Support for Java picks up the new Spring Boot 4.0.1 runtime and starts the server immediately.
> 
> 
> **3. Persistence & Custom Migration (api):**
> * **JPA Constraint:** All entities must use `@Column(columnDefinition = "TEXT")` for long-form strings (descriptions, learning outcomes) to support professional syllabi without truncation.
> * **Migration Engine:** Implement a custom `Migration.java` runner using `CommandLineRunner`. It must programmatically verify and create the schema on startup, replacing Flyway/Liquibase for maximum control.
> * **Data Seeding:** Include a seeding service that populates the `bsapi` database with the 44 specific courses categorized by the Bluestron Business Guide.
> 
> 
> **4. Full-Stack Integrity & UI (ui):**
> * **Type Safety:** Use **Zod** in the UI to define a shared contract with the Java entities.
> * **Mobile-First Design:** Build the `app/page.tsx` using a **Bento-Box** layout.
> * **Visual Hierarchy:** Training is the lead service (occupying primary grid real estate). Research, Data Analysis & Management, and Software Development must be distinct, subordinate sections.
> 
> 
> **5. Documentation & Best Practices:**
> * Implement **clean architecture**: Separate Controllers, Services, and Repositories.
> * Every file must include TSDoc/Javadoc explaining the architectural "Why" behind the "What."
> * Provide a **Verification Checklist** with percentage completion and automated health-check commands.
> 
> 
> **Output Requirement:** Generate all source codes (Dockerfile, Docker-compose, Makefile, Java Classes, Next.js Components, Zod Schemas, and properties files) in a copy-pasteable, modular format.

---

### Verification Checklist for this Prompt

| Category | Verification Item |
| --- | --- |
| **Scaffolding** | Does it include `mkdir` and initialization commands for both frameworks? |
| **API Version** | Is it locked to **Spring Boot 4.0.1** and **Java 21**? |
| **DB Name** | Is the database explicitly named **`bsapi`**? |
| **Persistence** | Is the `columnDefinition = "TEXT"` constraint explicitly mentioned? |
| **UI Logic** | Is the **Bento Grid** and **Training-as-Lead** hierarchy enforced? |
| **Ops** | Does the **Makefile** and **Docker-compose** bridge the entire system? |

**Section Summary:** This prompt moves the AI from "writing snippets" to "provisioning an environment." It forces the inclusion of the **Migration Runner** and the **VS Code Java sync** steps, which are usually the "missing links" in AI-generated code.

**What is Next?**
If you are satisfied with this "Golden Prompt," I will execute it immediately—starting with the **API persistence layer and the custom Migration engine** to ensure the database is ready for the 44-course seed.

**Shall I begin the full-stack generation based on this prompt?**