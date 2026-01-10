AFter applying 3-noTests.md in errors dir, it ave me some curl commands to verify everything is working as they should.















### Verification with curl and progress

You’re right—verifying with curl closes the loop. Below are ready-to-run curl commands for the core Module 1 endpoints (create/list/get), including homepage composites. After that, I’ll give a clear percentage of what’s left and proceed into Module 2 (Admin API with authentication and secure CRUD).

---

### Curl verification for module 1 endpoints

> Tip: replace values as needed. All endpoints assume `http://localhost:8080`.

#### Categories

- **Create category**
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "monitoring-evaluation",
    "name": "Monitoring & Evaluation",
    "description": "M&E courses",
    "status": "active"
  }'
```

- **List categories**
```bash
curl http://localhost:8080/api/categories
```

#### Courses

- **Create course**
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "advanced-me-health-programs",
    "title": "Advanced M&E and Data Management for Health Programs",
    "shortDescription": "Deep-dive into M&E for health.",
    "fullDescription": "Full syllabus...",
    "learningOutcomes": ["Design M&E frameworks", "Analyze health data"],
    "thumbnailUrl": "https://example.com/img/health-me.png",
    "categoryId": 1,
    "status": "published"
  }'
```

- **List courses (filters optional)**
```bash
curl "http://localhost:8080/api/courses?categoryId=1&status=published"
```

- **Get course by slug**
```bash
curl http://localhost:8080/api/courses/advanced-me-health-programs
```

#### Venues

- **Create venue**
```bash
curl -X POST http://localhost:8080/api/venues \
  -H "Content-Type: application/json" \
  -d '{
    "name": "View Park Towers",
    "city": "Nairobi",
    "address": "9th Floor, View Park Towers",
    "mapUrl": "https://maps.example.com/view-park"
  }'
```

- **List venues**
```bash
curl http://localhost:8080/api/venues
```

#### Course instances (cohorts)

- **Create cohort**
```bash
curl -X POST http://localhost:8080/api/courses/advanced-me-health-programs/instances \
  -H "Content-Type: application/json" \
  -d '{
    "mode": "in_person",
    "startDate": "2026-02-10",
    "endDate": "2026-02-14",
    "capacity": 25,
    "status": "open",
    "venueId": 1
  }'
```

- **List cohorts for course**
```bash
curl http://localhost:8080/api/courses/advanced-me-health-programs/instances
```

#### Registrations

- **Register for course/cohort**
```bash
curl -X POST http://localhost:8080/api/registrations \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": 1,
    "courseInstanceId": 1,
    "fullName": "Jane Doe",
    "email": "jane@example.com",
    "phone": "+254700000000",
    "organization": "Acme NGO",
    "role": "M&E Officer",
    "paymentOption": "online"
  }'
```

- **Get registration by id**
```bash
curl http://localhost:8080/api/registrations/1
```

#### Services

- **Create service offering**
```bash
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "baseline-studies",
    "title": "Baseline Studies",
    "description": "Baseline design and execution",
    "category": "research",
    "status": "active"
  }'
```

- **List services**
```bash
curl http://localhost:8080/api/services
```

- **Service enquiry**
```bash
curl -X POST http://localhost:8080/api/services/baseline-studies/enquiries \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Smith",
    "email": "john@example.com",
    "phone": "+254711111111",
    "organization": "Global Dev",
    "message": "We need a baseline in Kiambu."
  }'
```

#### Blog

- **Create blog post (markdown)**
```bash
curl -X POST http://localhost:8080/api/blog \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "data-privacy-essentials",
    "title": "Data Privacy Essentials",
    "summary": "Key practices for data privacy",
    "markdown": "# Privacy\\nBest practices...",
    "thumbnailUrl": "https://example.com/img/privacy.png",
    "status": "published"
  }'
```

- **List blog posts**
```bash
curl "http://localhost:8080/api/blog?status=published"
```

- **Get blog post by slug**
```bash
curl http://localhost:8080/api/blog/data-privacy-essentials
```

#### Homepage content

- **Create trainer**
```bash
curl -X POST http://localhost:8080/api/trainers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dr. Amina",
    "title": "Lead Trainer, M&E",
    "bio": "15 years in health M&E.",
    "imageUrl": "https://example.com/img/amina.png",
    "socialLinks": {"linkedin": "https://linkedin.com/in/amina"},
    "status": "active"
  }'
```

- **Create testimonial**
```bash
curl -X POST http://localhost:8080/api/testimonials \
  -H "Content-Type: application/json" \
  -d '{
    "authorName": "Grace W.",
    "authorRole": "Program Manager",
    "content": "Bluestron transformed our M&E practice.",
    "imageUrl": "https://example.com/img/grace.png",
    "status": "active"
  }'
```

- **Feature a course on homepage**
```bash
curl -X POST http://localhost:8080/api/featured \
  -H "Content-Type: application/json" \
  -d '{
    "kind": "course",
    "refId": 1,
    "position": 1,
    "active": true
  }'
```

- **Homepage composite**
```bash
curl http://localhost:8080/api/homepage
```

---

### How much is left (percentage)

- **Module 1 (Core domain, endpoints, migrations):** ~90% complete. Remaining ~10%: full DTO coverage for all entities, MapStruct mappers, and comprehensive unit tests.
- **Module 2 (Admin API: AuthN/AuthZ, secure CRUD, audit propagation):** ~0% complete—this is next.
- **Module 3 (Admin React UI):** 0%—after Module 2.
- **Module 4 (Public React UI):** 0%—after Module 3.
- **Module 5 (Payments & notifications):** 0%—Paystack + Gmail API templates after public/admin flows.

Overall system: ~18–22% complete (Module 0 + most of Module 1).

---

### Proceeding to module 2 — admin API with authentication

We’ll add secure CRUD for all entities with role-based access and audit propagation. Here’s the plan and immediate deliverables.

#### Scope

- **AuthN:** JWT-based login (username/password), token issuance, refresh.
- **AuthZ:** Roles—`ADMIN`, `EDITOR`, `VIEWER`.  
  - `ADMIN`: full CRUD across all entities.  
  - `EDITOR`: content CRUD (courses, trainers, testimonials, blog), no schema ops.  
  - `VIEWER`: read-only endpoints.
- **Audit propagation:** `created_by`, `updated_by` set from authenticated principal.
- **Secure endpoints:** `/api/admin/**` for admin CRUD; public endpoints remain read-only.

#### Backend changes

- **Entities:** `User`, `Role`, `Permission` (simple role model).
- **Security config:** JWT filter, authentication manager, password encoder (BCrypt).
- **Controllers:**  
  - `AuthController` → `/api/auth/login`, `/api/auth/refresh`.  
  - `Admin*Controller` → secure CRUD for all entities (courses, categories, instances, venues, registrations, services, blog, trainers, testimonials, featured).

#### Quick curl flow for admin (preview)

- **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"CHANGE_ME"}'
```
Response:
```json
{ "accessToken": "JWT_TOKEN", "refreshToken": "REFRESH_TOKEN" }
```

- **Create trainer (admin)**
```bash
curl -X POST http://localhost:8080/api/admin/trainers \
  -H "Authorization: Bearer JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dr. Amina",
    "title": "Lead Trainer, M&E",
    "bio": "15 years in health M&E.",
    "imageUrl": "https://example.com/img/amina.png",
    "socialLinks": {"linkedin": "https://linkedin.com/in/amina"},
    "status": "active"
  }'
```

- **Publish blog post (admin)**
```bash
curl -X PUT http://localhost:8080/api/admin/blog/data-privacy-essentials/status \
  -H "Authorization: Bearer JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"published"}'
```

---

### Immediate next deliverables

- Full Module 2 code:
  - **Auth entities, repositories, services, controllers**
  - **JWT security configuration**
  - **Admin CRUD controllers for all entities**
 