This exhaustive **Bluestron System Specification** acts as your high-fidelity blueprint. It converts every line from the **Bluestron Training Business Guide** into exact technical values and verification steps.

---

### 📋 The Bluestron "Supreme Architecture" Master Checklist

This guide is structured by **Dependency Priority** (Layer 1 must be verified before Layer 2 can exist).

#### Layer 1: Infrastructure & Data Persistence (Foundation)

| Requirement from Doc | Exact Technical Value | Verification Method |
| --- | --- | --- |
| **Secure Persistence** | PostgreSQL Database Name: `bsapi`. | Run `docker exec -it bluestron-db psql -U bluestron -d bsapi`. |
| **Data Integrity** | All long-form fields (Syllabus, Bio, Settings) MUST use `columnDefinition = "TEXT"`. | Run `\d courses` in psql; verify `description` column type is `text`, not `varchar(255)`. |
| **Dev Environment** | **Spring Boot 4.0.1** and **Java 21**. | Run `mvn -v` and check the console output for `Java version: 21` and `Spring Boot 4.0.1`. |
| **CORS Access** | Allowed Origins: `http://localhost:3000` (for UI-to-API communication). | Run `curl -I -X OPTIONS http://localhost:8080/api/courses` and check for `Access-Control-Allow-Origin`. |

#### Layer 2: The Training Hub (44 Courses & Resources)

| Requirement from Doc | Exact Technical Value | Verification Method |
| --- | --- | --- |
| **Course Volume** | Exactly **44 Courses** across 5 Categories. | Query API: `GET /api/courses/count`; verify response is `44`. |
| **Resource Support** | Teacher uploads for **Quizzes, PDF Notes, and Videos**. | Verify `CourseResource` entity exists with `resourceType` ENUM: `[QUIZ, PDF, VIDEO]`. |
| **Global Settings** | Dynamic control for Site Logo and Navigation Text. | Update the `site_settings` table via API; refresh UI to see the logo change instantly. |

#### Layer 3: Conversion & Business Logic (The Workflow)

| Requirement from Doc | Exact Technical Value | Verification Method |
| --- | --- | --- |
| **Registration Form** | Fields: Name, Email, Phone, Org, Role, Course, Venue, **Payment Option**. | Submit registration via UI; check `registrations` table for all 8 fields. |
| **Payment Logic** | **Paystack** for online; **Invoice Me** for offline tracking. | Select "Online"; verify redirection to `checkout.paystack.com`. |
| **Notifications** | **Dual Email**: One to Trainee, One to `info@bluestron.co.ke`. | Check the SMTP logs or Mailtrap to confirm two separate emails were fired. |

---

### 🎨 Design & Identity Specifications

| Element | Value / Implementation |
| --- | --- |
| **Primary Palette** | Navy Blue (`#000080`) and Bluestron Orange. |
| **Background** | "Blue Light Filter" Soft White (`#F9FAFB`) for high-legibility. |
| **Visual Hierarchy** | **Bento Grid**: Training Hero (Lead) |
| **Location** | View Park Towers, 9th Floor, Nairobi. |

---

### 🏗️ Verification Command "God-Mode"

Use this `Makefile` logic to verify the entire ecosystem in one "All-Nighter" push:

```bash
# Verify Infrastructure
make check-db      # Confirms postgres:16 is healthy on port 5433
make check-api     # Confirms Spring Boot 4.0.1 is live on port 8080

# Verify Data
make check-courses # Confirms 44 courses + TEXT column types exist
make check-seed    # Verifies the 5 categories are populated

```

### 💡 Senior Pro-Tip: The "Santa" Deployment

To achieve the "Santa Clause" global delivery effect you described, we will use **Next.js ISR (Incremental Static Regeneration)**.

* **The Value:** When the Admin updates a Course's price or a Logo in the CMS at 3:00 AM, the changes are "delivered" to all global users instantly without you needing to rebuild the entire application.

**Would you like me to output the complete source code for "Module 1: The Foundation" (CORS, Security, and Schema) to begin these verification tests?**

[What is Spring Boot 4.0?](https://www.youtube.com/watch?v=8OosRfWLEgg)
This video explains the key features and changes coming with the Spring Boot 4.0 release, which is the foundation of the Bluestron backend.
