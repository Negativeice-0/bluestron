Below is a **revised, hardened Expectations + Prompt system** whose sole purpose is **determinism**:
same input → same modules → same files → same code shape → same wiring → no drift, no “creative” reinterpretation, no hallucinated glue.

This is written as an **engineering contract**, not prose. It is intentionally exhaustive and restrictive so that **any competent AI, in any environment**, will converge on the **same output**.

---

# REVISED EXPECTATIONS

## (Bluetsron Business–Aligned, Deterministic Build Contract)

---

## 0. PRIMARY OBJECTIVE (NON-NEGOTIABLE)

The system **must** allow the user to:

1. Paste **one master prompt**
2. Paste **one business module specification** (Bluetsron business requirement)
3. Receive:

   * The **same folder structure**
   * The **same files**
   * The **same class names**
   * The **same method signatures**
   * The **same comments**
   * The **same wiring paths**
4. Every time, regardless of:

   * AI model
   * Provider
   * Context window
   * Temperature defaults

Creativity is **explicitly forbidden** outside business logic internals.

---

## 1. BLUETSRON BUSINESS REQUIREMENTS → TECH MAPPING

Bluetsron businesses are treated as **bounded contexts**, never features.

Each Bluetsron business requirement maps to:

```
ONE business domain
→ ONE backend module
→ ONE frontend module
→ ONE API contract
→ ONE database schema namespace
```

No cross-pollination unless explicitly declared.

---

## 2. GLOBAL STACK (LOCKED)

### Backend

* Spring Boot **3.5.9**
* Java **21**
* Gradle (Kotlin DSL)
* REST only (JSON)
* No Lombok unless explicitly allowed
* No reactive stack unless explicitly requested

### Frontend

* Next.js (latest stable)
* App Router only
* TypeScript (strict)
* No Redux unless explicitly allowed
* API calls via fetch (no axios by default)

### Database

* PostgreSQL **18**
* Schema-per-business-module
* No Flyway
* Custom migration engine ONLY

---

## 3. ABSOLUTE RULES FOR AI BEHAVIOR

The AI **MUST**:

1. Use **exact paths**
2. Use **exact names**
3. Include **stubs even if logic is empty**
4. Include **comments explaining intent and linkage**
5. Never rename or “improve” structure
6. Never collapse modules
7. Never skip files “for brevity”
8. Stop after each module

The AI **MUST NOT**:

* Ask design questions mid-module
* Assume business logic
* Introduce libraries
* Optimize
* Refactor
* Change naming conventions

---

## 4. CANONICAL PROJECT STRUCTURE (IMMUTABLE)

### Backend Root

```
backend/
├── build.gradle.kts
├── settings.gradle.kts
├── src/main/java/com/bluetsron/
│   ├── BluetsronApplication.java
│   ├── shared/
│   │   ├── config/
│   │   ├── migration/
│   │   ├── security/
│   │   ├── util/
│   ├── modules/
│   │   └── {business_name}/
│   │       ├── domain/
│   │       ├── application/
│   │       ├── infrastructure/
│   │       └── api/
│   └── bootstrap/
│       └── MigrationBootstrap.java
└── src/main/resources/
    ├── application.yml
    └── migrations/
        └── {business_name}/
```

### Frontend Root

```
frontend/
├── app/
│   ├── layout.tsx
│   ├── page.tsx
│   └── modules/
│       └── {business_name}/
│           ├── page.tsx
│           ├── components/
│           ├── hooks/
│           ├── services/
│           └── types.ts
├── lib/
└── public/
```

This structure **cannot be altered**.

---

## 5. BUSINESS MODULE CONTRACT (BLUETSRON)

Every Bluetsron business requirement **must be rewritten into this format before coding**:

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

The AI **must not invent** missing items.
If missing → stub + TODO comment.

---

## 6. BACKEND MODULE BLUEPRINT (FIXED)

For each business module `{business_name}`:

### Domain Layer

```
domain/
├── model/
│   └── {Entity}.java
├── repository/
│   └── {Entity}Repository.java
```

* Entities contain NO framework annotations except JPA
* Repositories are interfaces only

### Application Layer

```
application/
├── service/
│   └── {UseCase}Service.java
├── dto/
│   └── {Request|Response}.java
```

* Contains business logic orchestration
* No controllers
* No persistence code

### Infrastructure Layer

```
infrastructure/
├── persistence/
│   └── Jpa{Entity}Repository.java
```

* Implements repositories
* Handles database mapping

### API Layer

```
api/
└── {Business}Controller.java
```

* Pure REST
* Delegates immediately to Application layer

---

## 7. CUSTOM MIGRATION ENGINE (MANDATORY)

### Structure

```
shared/migration/
├── Migration.java
├── MigrationRunner.java
└── MigrationRegistry.java
```

### Rules

* Each migration:

  * Version
  * Description
  * Checksum
  * SQL content
* Stored in:

```
resources/migrations/{business_name}/V{number}__description.sql
```

### Startup Flow

```
BluetsronApplication
→ MigrationBootstrap
→ MigrationRunner
→ PostgreSQL
```

AI must include:

* Stub SQL
* Java execution logic
* Commented flow

---

## 8. FRONTEND MODULE BLUEPRINT (FIXED)

Each frontend business module mirrors backend:

```
modules/{business_name}/
├── page.tsx
├── services/
│   └── {business}.api.ts
├── hooks/
│   └── use{Business}.ts
├── components/
└── types.ts
```

* services call backend paths EXACTLY
* types mirror backend DTOs
* hooks contain orchestration only

---

## 9. CROSS-SYSTEM TRACEABILITY (CRITICAL)

Every file must contain a header comment:

```java
// Bluetsron Business Module: {business_name}
// Layer: {domain|application|infrastructure|api}
// Depends on: {explicit classes}
// Used by: {explicit classes}
```

Frontend equivalent in TS comments.

This ensures **handoff survivability**.

---

## 10. STOP CONDITIONS

The AI must stop after:

* Architecture definition
* Each backend module
* Each frontend module
* Migration setup

And ask:

> “Approve `{module_name}` before proceeding?”

---

# FINAL MASTER PROMPT (PASTE-READY)

This is the **single prompt** you paste into any AI.

---

```
You are a deterministic software construction engine.

Your task is to implement Bluetsron business modules EXACTLY as specified,
with zero creativity and zero deviation.

GLOBAL CONSTRAINTS
- Follow the Bluetsron Deterministic Build Contract.
- Do not invent structure, names, or libraries.
- Produce the same output for the same input every time.
- Include stubs, TODOs, and comments where logic is undefined.
- Use full paths and explicit wiring comments.

STACK (LOCKED)
Backend:
- Spring Boot 3.5.9
- Java 21
- PostgreSQL 18
- Custom migration engine (Flyway forbidden)

Frontend:
- Next.js App Router
- TypeScript strict

PROCESS
1. Read the provided BUSINESS MODULE SPECIFICATION.
2. Map it to exactly one backend module and one frontend module.
3. Generate:
   - Architecture explanation
   - Directory tree
   - Stubbed files with comments
4. STOP and wait for approval.

DO NOT:
- Rename folders
- Skip files
- Optimize
- Ask questions mid-module

BEGIN ONLY AFTER THE BUSINESS MODULE SPECIFICATION IS PROVIDED.
```

---

## Final Note (Plain Truth)

What you have now is **not a prompt**.
It is a **reproducible software manufacturing protocol**.

If you want, next we can:

* Convert this into `expectations.txt`
* Generate a **Bluetsron sample business module**
* Stress-test it across different AIs for drift

Say the word.
