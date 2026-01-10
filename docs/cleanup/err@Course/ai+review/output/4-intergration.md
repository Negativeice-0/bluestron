This is a **valid complaint**. What you are missing is not more prose — you are missing a **single, human-memorable control surface** that lets you:

* Know **all Bluetsron requirements at a glance**
* Apply **any pro prompt** to **any AI**
* Verify compliance **without rereading everything**
* Catch drift **mechanically**, not cognitively

Below is exactly that.

---

# BLUETSRON MASTER CHECKLIST

## (Memory Anchor + AI Drift Firewall)

This section is designed so you can remember **90% of Bluetsron** with **one page**.

If an AI violates **any unchecked item**, the output is invalid.

---

## A. BLUETSRON CORE LAWS (MEMORIZE THESE 7)

You only need to remember **7 laws**. Everything else derives from them.

### LAW 1 — ONE BUSINESS = ONE BOUNDED CONTEXT

* One backend module
* One frontend module
* One DB schema
* One API surface

❌ No shared business models

---

### LAW 2 — STRUCTURE IS IMMUTABLE

* Folder paths
* File names
* Layer names

❌ No renaming
❌ No collapsing
❌ No “better structure”

---

### LAW 3 — STACK IS LOCKED

Backend:

* Spring Boot **3.5.9**
* Java **21**
* PostgreSQL **18**
* Custom migrations (no Flyway)

Frontend:

* Next.js App Router
* TypeScript strict

❌ No new libraries unless approved

---

### LAW 4 — NO ASSUMPTIONS

* Missing business detail → stub + TODO
* Never infer behavior

---

### LAW 5 — TRACEABILITY EVERYWHERE

Every file must state:

* Business module
* Layer
* Depends on
* Used by

---

### LAW 6 — STEPWISE EXECUTION

* Module 0 → approve
* Module 1 → approve
* Stop after each module

---

### LAW 7 — SAME INPUT = SAME OUTPUT

* Determinism over elegance
* Reproducibility over speed

---

If an AI follows these **7 laws**, it is Bluetsron-compliant.

---

## B. BLUETSRON FULL REQUIREMENTS CHECKLIST

### (Use This to Validate AI Output)

You do **not** need to remember this — just scan it.

---

### 1. PROJECT INITIALIZATION

#### Backend

* [ ] Spring Initializr used
* [ ] Group: `com.bluetsron`
* [ ] Artifact: `backend`
* [ ] Java 21
* [ ] Spring Boot 3.5.9
* [ ] Dependencies ONLY:

  * Web
  * JPA
  * PostgreSQL
  * Validation

#### Frontend

* [ ] `create-next-app`
* [ ] App Router enabled
* [ ] TypeScript strict
* [ ] No Tailwind initially

---

### 2. DIRECTORY STRUCTURE

#### Backend

* [ ] `shared/` exists
* [ ] `bootstrap/` exists
* [ ] `modules/{business}/` exists
* [ ] `migrations/{business}/` exists

#### Frontend

* [ ] `modules/{business}/` exists
* [ ] `services/`, `hooks/`, `types.ts` present

---

### 3. BUSINESS MODULE CONTRACT

* [ ] BUSINESS MODULE NAME
* [ ] BUSINESS PURPOSE
* [ ] PRIMARY ACTORS
* [ ] CORE OPERATIONS
* [ ] DATA OWNED
* [ ] EXTERNAL DEPENDENCIES
* [ ] FAILURE SCENARIOS
* [ ] NON-GOALS

Missing fields → stub + TODO

---

### 4. BACKEND LAYERS (PER BUSINESS)

* [ ] domain/

  * [ ] Entity
  * [ ] Repository interface
* [ ] application/

  * [ ] Service
  * [ ] DTOs
* [ ] infrastructure/

  * [ ] JPA repository impl
* [ ] api/

  * [ ] Controller

❌ No controller logic
❌ No persistence in application layer

---

### 5. MIGRATION ENGINE

* [ ] Custom runner exists
* [ ] SQL versioned files
* [ ] Idempotent SQL
* [ ] Executed on startup
* [ ] Schema per business

---

### 6. FRONTEND MODULE MIRRORING

* [ ] API paths exactly match backend
* [ ] Types mirror DTOs
* [ ] Hooks orchestrate logic only
* [ ] Components are dumb

---

### 7. TRACEABILITY HEADERS

Every file:

* [ ] Business name
* [ ] Layer
* [ ] Depends on
* [ ] Used by

---

### 8. STOP & VERIFY

* [ ] AI stopped after module
* [ ] Explicit approval requested

---

## C. HOW TO APPLY **ANY PRO PROMPT** (UNIVERSAL METHOD)

This is the part you were missing.

### 🔑 The 3-PART PROMPT STACK (MEMORIZE THIS)

You **always** paste prompts in this order:

---

### 1️⃣ CONTROL PROMPT (ALWAYS FIRST)

```
You are bound by the Bluetsron Deterministic Build Contract.
You must follow the 7 Bluetsron Core Laws.
Deviation invalidates the output.
```

---

### 2️⃣ CONTEXT PROMPT (README)

Paste:

* The Bluetsron README (canonical)
* The checklist (optional but recommended)

This removes interpretation.

---

### 3️⃣ TASK PROMPT (MODULE)

```
BUSINESS MODULE SPECIFICATION:
<insert module here>

Generate Module 0 only.
Stop and ask for approval.
```

---

### ❗ If you skip ANY of the three, drift WILL occur.

---

## D. HOW TO REMEMBER THIS WITHOUT MENTAL OVERLOAD

You do **not** remember everything.
You remember **anchors**.

### Memorize ONLY:

* **7 Core Laws**
* **3-Part Prompt Stack**
* **One rule**: “Structure over creativity”

Everything else lives in README + checklist.

---

## E. QUICK DRIFT DETECTION (30 SECONDS)

Ask yourself:

1. Did it rename anything?
2. Did it add a library?
3. Did it infer behavior?
4. Did it skip stubs?
5. Did it proceed without approval?

If **yes** to any → reject output.

---

## F. FINAL RECOMMENDATION (IMPORTANT)

Put this in your repo root:

```
/README.md
/BLUETSRON_CHECKLIST.md
/MASTER_PROMPT.txt
```

Never prompt an AI without attaching all three.

That is how you “remember everything”
**without actually remembering everything.**

---

If you want next, I can:

* Compress this into a **1-page printable card**
* Generate **MASTER_PROMPT.txt** exactly
* Create a **Bluetsron compliance linter (manual or script)**

Tell me which.
