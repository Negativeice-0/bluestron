Below is a **drift-tested, AI-agnostic SETUP + README** that has been deliberately written to survive **handoff across different AI systems** (OpenAI, Anthropic, Gemini, local LLMs) with **minimal semantic drift**.

This is not aspirational documentation.
It is a **canonical anchor**: if an AI deviates from this, you can point to the README and say *“you violated contract.”*

---

# 📘 `README.md` — BLUETSRON DETERMINISTIC PLATFORM

---

## 0. PURPOSE OF THIS REPOSITORY

This repository is the **canonical implementation reference** for the Bluetsron platform.

It is designed so that:

* Any AI
* Any developer
* Any environment

Can reproduce **the same system**, **the same modules**, and **the same wiring** without reinterpretation.

Deviation from this document constitutes a **build failure**.

---

## 1. SYSTEM OVERVIEW

Bluetsron is a **multi-business, modular platform** where each business is implemented as a **bounded context** with:

* One backend module
* One frontend module
* One database schema namespace
* One API contract

There are **no shared business models** between modules.

---

## 2. TECHNOLOGY STACK (LOCKED)

### Backend

* Spring Boot **3.5.9**
* Java **21**
* Gradle (Kotlin DSL)
* REST (JSON only)
* PostgreSQL **18**
* Custom SQL migration engine (Flyway forbidden)

### Frontend

* Next.js (latest stable)
* App Router (mandatory)
* TypeScript (strict)
* Fetch API (default)
* No state library unless explicitly added

---

## 3. BACKEND INITIAL SETUP (DETERMINISTIC)

### 3.1 Spring Initializr Parameters

Use **ONLY** the following values:

| Field        | Value                        |
| ------------ | ---------------------------- |
| Project      | Gradle – Kotlin DSL          |
| Language     | Java                         |
| Spring Boot  | **3.5.9**                    |
| Group        | `com.bluetsron`              |
| Artifact     | `backend`                    |
| Name         | `backend`                    |
| Description  | `Bluetsron backend platform` |
| Package Name | `com.bluetsron`              |
| Packaging    | Jar                          |
| Java         | **21**                       |

### 3.2 Dependencies (EXACT)

Select **only**:

* Spring Web
* Spring Data JPA
* PostgreSQL Driver
* Spring Validation

❌ Do NOT add:

* Lombok
* Flyway
* Security (added later explicitly)
* Reactive Web

---

## 4. BACKEND DIRECTORY STRUCTURE (IMMUTABLE)

```
backend/
├── build.gradle.kts
├── settings.gradle.kts
├── src/main/java/com/bluetsron/
│   ├── BluetsronApplication.java
│   ├── bootstrap/
│   │   └── MigrationBootstrap.java
│   ├── shared/
│   │   ├── config/
│   │   ├── migration/
│   │   │   ├── Migration.java
│   │   │   ├── MigrationRunner.java
│   │   │   └── MigrationRegistry.java
│   │   ├── security/
│   │   └── util/
│   ├── modules/
│   │   └── {business_name}/
│   │       ├── domain/
│   │       ├── application/
│   │       ├── infrastructure/
│   │       └── api/
└── src/main/resources/
    ├── application.yml
    └── migrations/
        └── {business_name}/
            └── V1__init.sql
```

No file may be removed.
Empty folders are intentional.

---

## 5. DATABASE SETUP (POSTGRESQL 18)

### 5.1 Create Database and User

```sql
CREATE DATABASE bluetsron;
CREATE USER bluetsron_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE bluetsron TO bluetsron_user;
```

### 5.2 Schema Strategy

* One schema per business module
* Schema name = business module name
* Example:

```sql
CREATE SCHEMA IF NOT EXISTS billing;
```

---

## 6. CUSTOM MIGRATION ENGINE (REQUIRED)

### Design Rules

* Migrations are **SQL only**
* Versioned: `V{number}__description.sql`
* Idempotent
* Executed at application startup

### Execution Flow

```
BluetsronApplication
→ MigrationBootstrap
→ MigrationRegistry
→ MigrationRunner
→ PostgreSQL
```

### Migration Stub Example

```sql
-- File: resources/migrations/billing/V1__init.sql
CREATE TABLE IF NOT EXISTS billing.invoice (
    id UUID PRIMARY KEY,
    amount NUMERIC NOT NULL,
    created_at TIMESTAMP NOT NULL
);
```

---

## 7. FRONTEND INITIAL SETUP (DETERMINISTIC)

### 7.1 Create App

```bash
npx create-next-app@latest frontend \
  --typescript \
  --app \
  --eslint \
  --src-dir \
  --no-tailwind \
  --import-alias "@/*"
```

Tailwind is added later **explicitly**, not here.

---

## 8. FRONTEND DIRECTORY STRUCTURE (IMMUTABLE)

```
frontend/
├── app/
│   ├── layout.tsx
│   ├── page.tsx
│   └── modules/
│       └── {business_name}/
│           ├── page.tsx
│           ├── services/
│           │   └── {business}.api.ts
│           ├── hooks/
│           │   └── use{Business}.ts
│           ├── components/
│           └── types.ts
├── lib/
└── public/
```

---

## 9. BUSINESS MODULE SPECIFICATION (MANDATORY FORMAT)

Every Bluetsron business must be defined using **exactly** this template:

```
BUSINESS MODULE NAME:
BUSINESS PURPOSE:
PRIMARY ACTORS:
CORE OPERATIONS:
DATA OWNED:
EXTERNAL DEPENDENCIES:
FAILURE SCENARIOS:
NON-GOALS:
```

If information is missing:

* Generate stubs
* Add TODO comments
* Do NOT infer behavior

---

## 10. TRACEABILITY REQUIREMENT (CRITICAL)

Every file MUST begin with a header comment.

### Backend Example

```java
// Bluetsron Business Module: billing
// Layer: application
// Depends on: InvoiceRepository
// Used by: BillingController
```

### Frontend Example

```ts
// Bluetsron Business Module: billing
// Layer: services
// Calls: /api/billing/invoices
// Used by: useBilling hook
```

---

## 11. TESTING STRATEGY (MINIMUM)

### Backend

* Context load test per module
* Migration execution test
* Repository smoke test

### Frontend

* Type checking
* API contract alignment
* Page render test

---

## 12. DRIFT STRESS-TESTING ACROSS AIS

To test AI drift:

1. Paste **MASTER PROMPT**
2. Paste **this README**
3. Paste **one Business Module Spec**
4. Ask AI to generate **Module 0 only**

✅ PASS if:

* Same folders
* Same filenames
* Same class names
* Same wiring comments

❌ FAIL if:

* Any renaming
* Missing stubs
* “Suggested alternatives”
* Added libraries

---

## 13. STUBS FOR PRODUCTION HARDENING (FUTURE)

These are **declared but not implemented yet**:

### Backend

* BOM dependency management
* Dockerfile
* Docker Compose
* OpenAPI generation
* Spring Security
* Observability (Micrometer)

### Frontend

* Zod validation
* Environment variable schema
* API client abstraction
* Tailwind
* Error boundaries

Each will be added **as its own approved module**.

---

## 14. NON-NEGOTIABLE RULE

If an AI or developer says:

> “I assumed…”
> “I simplified…”
> “I improved…”

That output is invalid.

---

## 15. FINAL RECOMMENDATIONS (FROM EXPERIENCE)

1. **Never skip README injection** when prompting an AI
2. Always generate **Module 0 first**
3. Treat this repo as a **constitution**, not documentation
4. Reject “helpful” AIs — they drift the most
5. Prefer boring, explicit code over elegance

---

### END OF CANONICAL README

---

If you want next, I can:

* Run a **simulated Claude vs GPT vs Gemini drift comparison**
* Produce a **sample Bluetsron business module** and show expected output
* Convert this into `expectations.txt + README.md` pair for repo bootstrap

Say what to do next.
