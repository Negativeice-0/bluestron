I understand perfectly. You are building this for the long term—a system that makes the Bluestron team feel every word of their vision has been translated into a high-performance, Kenyan-market-ready digital asset.

The following **"Supreme Execution Prompt"** is built to be exhaustive, referencing every detail from the 9-page guide  and layering in the production constraints we've established.

---

### The Bluestron "Supreme Execution" Master Prompt

> **Role:** You are the Lead Architect and Full-Stack Developer commissioned to build the official **Bluestron** platform.
> 
> 
> **Objective:** Deliver a beautiful, scalable, mobile-first system that strictly adheres to the "Bluestron Training Business" guide .
> 
> 
> **1. Core Architectural Specs:**
> * **Stack:** Spring Boot 4.0.1 (Java 21) and Next.js 14 (App Router).
> * **Database:** PostgreSQL named `bsapi` on a Docker network.
> * **Persistence Constraint:** Use `@Column(columnDefinition = "TEXT")` for all long-form fields (Course Descriptions, Learning Outcomes, Service Case Studies).
> * **Automation:** A root-level **Makefile** for `up`, `down`, `build`, `migrate`, and `seed-all`.
> 
> 
> **2. Content & Business Logic (Satiating the Document):**
> * **Hierarchy:** Training is the lead service. Research, Data Analysis, and Software are "distinct but visually subordinate".
> 
> 
> * **Training Module:** Seed the system with the 44 courses categorized into:
> 1. Project Management / M&E (16 courses) .
> 2. Data Management & Analysis (12 courses) .
> 3. GIS & IT / Software / Mobile Data (3 courses) .
> 4. Management & HR (10 courses) .
> 5. Climate Change / Environment (3 courses) .
> 
> 
> * **The Registration Workflow:**
> - Every course page must have a prominent **"REGISTER NOW"** button.
> - **The Form:** Captures Full Name, Email, Phone, Organization, Role, Pre-populated Course, and **Payment Option** (Online Payment / Invoice Me) .
> - **Post-Submission:** Show "Thank You" message and trigger dual-email (Trainee & Internal Admin) with specific next steps .
> 
> 
> 
> 
> **3. Essential Features & Integrations (Non-Negotiable):**
> - **Custom CMS/Admin Dashboard:** A local-auth-secured area (Username/Password, no Google Auth) to manage all courses, service descriptions, blog posts, and trainee registrations.
> - **Payment Gateway:** Implementation for **Paystack** for the "Online Payment" option.
> - **Email Engine:** Local SMTP configuration for automated registration and enquiry notifications .
> - **Services:** Dedicated, visually consistent pages for Research (Baseline/Midline/Endline), Data Analysis (Cleaning/Visualization), and Software Development (Custom Web/Mobile) .
> 
> 
> * **SEO & Analytics:** Full meta-tag support and Google Analytics integration.
> - **Contact & News:** Full contact form, physical address (View Park Towers), and newsletter sign-up .
> 
> 
> 
> 
> **4. Developer Experience (DX):**
> - Provide a `MigrationRunner.java` to programmatically build the schema and seed all 44 courses .
> 
> 
> * Ensure Next.js uses **Zod** to validate types against the Java DTOs for 100% full-stack integrity.
> 
> 
> **Deliverable:** Full, well-commented source code across the `api`, `ui`, and `ops` directories.

---

### Module 0: The Infrastructure Setup

#### 1. The API Dockerfile (`api/Dockerfile`)

```dockerfile
# Multi-stage build for a Senior Architect workflow
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

```

#### 2. The Global Orchestration (`ops/docker-compose.yml`)

```yaml
version: '3.8'
services:
  db:
    image: postgres:16
    container_name: bluestron-db
    environment:
      POSTGRES_USER: bluestron
      POSTGRES_PASSWORD: password
      POSTGRES_DB: bsapi
    ports:
      - "5433:5432"
    networks:
      - bluestron-network

  api:
    build: ../api
    container_name: bluestron-api
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/bsapi
      SPRING_DATASOURCE_USERNAME: bluestron
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    depends_on:
      - db
    networks:
      - bluestron-network

networks:
  bluestron-network:
    driver: bridge

```

---

### Progress & Verification

**Project Completion: 10%**

| Check | Expected Result | Reference |
| --- | --- | --- |
| **Initial folders** | `mkdir api ui ops` |  |
| **Database Network** | `make up` starts `bluestron-db` and `bluestron-api` |  |
| **Persistence Integrity** | Course descriptions in DB show type `text` |  |
| **Visual Hierarchy** | Home Page hero text: "Professional Training..."

 |  |

**What is Next?**
I am ready to execute the **Full Persistence Layer**. This includes the **44-course seed logic** in the `MigrationRunner.java` and the **Registration Entity** with the payment option fields .

**Shall I proceed with the full source for the Course & Registration Backend?**