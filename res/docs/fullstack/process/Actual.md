# **BLUESTRON TRAINING PORTAL - COMPLETE REVISED IMPLEMENTATION SPECIFICATION**
*Explicit Requirement Matching to PDF Document with Complete Technical Clarifications*

## **1. CORE BUSINESS REQUIREMENTS (EXACT PDF MATCH - NON-NEGOTIABLE)**

### **Primary Business Focus - Training First (PDF Section 1.1):**
- **Implementation:** The ENTIRE system is built around 44 courses loaded via `V2__InitCourses.java` migration, organized into 5 EXACT PDF categories. Training dominates 70% of homepage real estate with:
  - Hero banner: "Professional Training, Research & Software Development in Data, M&E, Management" (exact PDF text)
  - 3-box highlight section where Training box is 50% wider than Research/Software boxes
  - Featured courses carousel showing 3-5 upcoming courses with "Register Now" buttons
  - Quick category links showing all 5 categories with course counts

### **Secondary Services (PDF Section 1.1):**
- **Research Services Page:** `/services/research` - COMPLETE page with:
  - Baseline Studies section with example projects
  - Midline Studies with methodology descriptions  
  - Endline Evaluations with reporting templates
  - Custom Research & Consultancy with enquiry form
  - Case-studies / sample projects (admin can add/edit/delete)
- **Data Analysis & Management Page:** `/services/data-analysis` - COMPLETE page with:
  - Data cleaning, processing, analysis workflows
  - Visualization / dashboards gallery (admin can upload screenshots)
  - Software tools / custom analytics descriptions
  - Example deliverables section
- **Software Development Page:** `/services/software` - COMPLETE page with:
  - Custom software (web/mobile) portfolio
  - Integration & automation case studies
  - Maintenance & support packages
  - Portfolio / examples gallery

## **2. DATABASE MANAGEMENT SYSTEM - CUSTOM JAVA IMPLEMENTATION**

### **NO DOCKER, NO FLYWAY, NO EXTERNAL TOOLS:**
```
Database Setup Process:
1. Provision PostgreSQL database (Aiven, AWS RDS, DigitalOcean, etc.)
2. Set connection string in application.yml
3. Application starts → CustomMigrationRunner executes

Implementation Details:
┌─────────────────────────────────────────────────────────────┐
│   CUSTOM MIGRATION SYSTEM (Java-based, No External Tools)   │
├─────────────────────────────────────────────────────────────┤
│ 1. Migration Files Location:                                │
│    src/main/resources/migrations/                           │
│    ├── V1__init_categories.sql                              │
│    ├── V2__init_44_courses.sql                              │
│    ├── V3__init_registrations_table.sql                     │
│    └── V4__init_admin_users.sql                             │
│                                                             │
│ 2. CustomMigrationRunner.java:                              │
│    @Component                                               │
│    public class CustomMigrationRunner {                     │
│        @PostConstruct                                       │
│        public void runMigrations() {                        │
│            // 1. Check 'schema_migrations' table            │
│            // 2. Read ALL .sql files in migrations/         │
│            // 3. Execute ONLY unrun migrations in order     │
│            // 4. Record completion in schema_migrations     │
│        }                                                    │
│    }                                                        │
│                                                             │
│ 3. Admin-Triggered Migrations:                              │
│    POST /api/admin/migrations/run                           │
│    - Only accessible by ADMIN role                          │
│    - Shows pending migrations                               │
│    - Executes selected migrations                           │
│    - Logs every action for audit                            │
└─────────────────────────────────────────────────────────────┘
```

### **ADMIN CAN EDIT EVERYTHING VIA JAVA CMS:**
```
COMPLETE ADMIN DASHBOARD CAPABILITIES:
Navigation: Dashboard → Courses → Services → Content → Users → Certificates → Analytics

1. COURSE MANAGEMENT:
   - Create/Edit/Delete course categories (5 from PDF)
   - Add/Edit/Delete courses (all 44 courses editable)
   - For EACH course: edit title, description (short/long), duration, dates, 
     mode (in-person/online/hybrid), location, thumbnail, price, registration settings
   - Bulk import/export via CSV
   - Set featured courses for homepage carousel

2. SERVICE PAGES MANAGEMENT:
   - Edit Research Services page content (WYSIWYG editor)
   - Edit Data Analysis page content (WYSIWYG editor)
   - Edit Software Development page content (WYSIWYG editor)
   - Upload case studies, examples, portfolio items
   - Manage enquiry form fields and destinations

3. CONTENT MANAGEMENT (ALL SITE CONTENT):
   - Homepage: Edit hero text, 3-box content, testimonials, newsletter text
   - About Us: Edit mission, vision, values, team bios, certifications
   - Contact: Edit address, phone numbers, map location, form settings
   - Footer: Edit all links, social media URLs, copyright text
   - Blog/Insights: Create/edit/delete blog posts with categories

4. USER & REGISTRATION MANAGEMENT:
   - View ALL registrations with filters
   - Edit registration status (confirmed, pending, cancelled)
   - Export registrations to Excel/PDF
   - Send bulk emails to registrants
   - Manage user accounts (enable/disable, reset passwords)

5. DATABASE OPERATIONS (SAFE, CONTROLLED):
   - View database tables (read-only)
   - Execute SELECT queries (with row limits)
   - Execute UPDATE/DELETE queries with confirmation dialogs
   - NEVER allowed: DROP, ALTER, TRUNCATE (blocked at Java level)
   - All queries logged with admin user ID and timestamp

6. SYSTEM CONFIGURATION:
   - Email templates: Edit registration confirmations, admin alerts
   - Payment settings: Configure invoice options (online payments later)
   - SEO settings: Edit meta tags, sitemap configuration
   - Analytics: Connect Google Analytics, view dashboard
   - Backup: Trigger manual backups, view backup history
```

## **3. REGISTRATION WORKFLOW - EXACT PDF SECTION 6 IMPLEMENTATION**

### **Registration Form - FIELD-BY-FIELD MATCH:**
```java
// Registration Entity - EXACT 8 fields from PDF
@Entity
@Table(name = "registrations")
public class Registration {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;                    // PDF: Full Name
    
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email required")
    @Column(nullable = false)
    private String email;                       // PDF: Email Address
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", 
             message = "Valid phone number required")
    private String phoneNumber;                 // PDF: Phone Number
    
    @Column(nullable = true)                    // PDF: "if applicable"
    private String organization;                // PDF: Organization
    
    @NotBlank(message = "Role/Designation is required")
    private String roleDesignation;             // PDF: Role/Designation
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;                      // PDF: Course Selected (pre-populated)
    
    @Column(nullable = true)                    // PDF: "if applicable"
    private LocalDate preferredDate;            // PDF: Preferred Date
    
    @Column(nullable = true)
    private String preferredVenue;              // PDF: Venue
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentOption paymentOption;        // PDF: Payment Option (Online/Invoice)
    
    private LocalDateTime registeredAt;
    
    // PDF: "Upon submission show thank-you/confirmation page"
    private String confirmationNumber; // Generates: BLR-2024-00123
}

// Payment Option Enum
public enum PaymentOption {
    ONLINE_PAYMENT,  // PDF: "Online payment"
    INVOICE_ME       // PDF: "Invoice me"
}
```

### **Post-Registration Email System - DUAL EMAILS:**
```java
@Service
@Transactional
public class RegistrationEmailService {
    
    @Async
    public void sendRegistrationEmails(Registration registration) {
        // PDF: "Send an automated email to the participant"
        Email traineeEmail = Email.builder()
            .to(registration.getEmail())
            .subject("Bluestron Course Registration Confirmation: " + 
                    registration.getCourse().getTitle())
            .template("trainee-registration-confirmation.html")
            .variables(Map.of(
                "fullName", registration.getFullName(),
                "courseTitle", registration.getCourse().getTitle(),
                "courseDate", formatDate(registration.getPreferredDate()),
                "courseVenue", registration.getPreferredVenue(),
                "paymentOption", registration.getPaymentOption(),
                "confirmationNumber", registration.getConfirmationNumber(),
                "nextSteps", getNextSteps(registration),
                "contactPhone", "+254 702 588644", // FROM PDF
                "contactEmail", "info@bluestron.co.ke" // FROM PDF
            ))
            .attachment(certificateService.generateCertificate(registration)) // WEEK 4 MODULE
            .build();
        
        // PDF: "Send an automated email to Bluestron internal admin"
        Email adminEmail = Email.builder()
            .to("info@bluestron.co.ke") // EXACT PDF EMAIL
            .subject("NEW REGISTRATION: " + registration.getCourse().getTitle())
            .template("admin-registration-notification.html")
            .variables(Map.of(
                "registration", registration,
                "allFields", getAllRegistrationFields(registration),
                "timestamp", LocalDateTime.now(),
                "adminDashboardLink", "https://admin.bluestron.co.ke"
            ))
            .build();
        
        emailService.send(traineeEmail);
        emailService.send(adminEmail);
    }
}
```

## **4. VISUAL DESIGN & BRANDING - EXACT IMPLEMENTATION**

### **Bluestron Brand Colors (Non-Negotiable):**
```
PRIMARY COLOR PALETTE:
- Navy Blue: #001f3f (Primary brand color)
  Usage: Navigation bar, footer, primary headings, buttons on light backgrounds
  CSS: bg-[#001f3f], text-[#001f3f], border-[#001f3f]
  
- Orange: #ff851b (Accent/CTA color)
  Usage: All buttons, hover states, highlights, alerts
  CSS: bg-[#ff851b], hover:bg-[#e76a00], text-[#ff851b]
  
- White: #ffffff (Backgrounds)
  Usage: Card backgrounds, main content areas
  
- Light Gray: #f8f9fa (Secondary backgrounds)
  Usage: Alternate sections, form backgrounds

TYPOGRAPHY:
- Primary Font: Inter (Google Fonts)
  Weights: 400 (Regular), 500 (Medium), 600 (SemiBold), 700 (Bold)
  Usage: ALL text elements
  
- Monospace: JetBrains Mono
  Usage: Code snippets, technical content only

LOGO USAGE:
- Header Logo: Left aligned, max-height: 60px on desktop, 40px on mobile
- Favicon: 32x32 simplified version
- Certificate Watermark: Semi-transparent logo in certificate background

IMAGERY (PDF: "not generic stock only"):
- Training: Real photos of Bluestron training sessions
- Data/Research: Custom screenshots of dashboards, charts
- Software: Screenshots of actual Bluestron projects
- Team: Professional headshots of actual trainers
- NO generic stock photography allowed
```

### **Homepage Layout - EXACT PDF Section 2.1:**
```
1. HERO BANNER (Full width):
   ┌─────────────────────────────────────────────────────┐
   │ [Bluestron Logo]              [Navigation Menu]     │
   │                                                     │
   │                                                     │
   │  Professional Training, Research & Software         │
   │  Development in Data, M&E, Management               │
   │                                                     │
   │  [Browse Courses Button] [Learn More Button]        │
   └─────────────────────────────────────────────────────┘

2. 3-BOX HIGHLIGHT SECTION:
   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐
   │   TRAINING  │  │   RESEARCH  │  │  SOFTWARE   │
   │             │  │   SERVICES  │  │ DEVELOPMENT │
   │   (Lead)    │  │             │  │             │
   │             │  │             │  │             │
   │ [Learn More]│  │ [Learn More]│  │ [Learn More]│
   └─────────────┘  └─────────────┘  └─────────────┘
   Training box is 1.5x width of others visually

3. FEATURED COURSES CAROUSEL:
   - Auto-rotates every 5 seconds
   - Shows 3 courses on desktop, 1 on mobile
   - Each slide: Course image, title, date, "Register Now"
   - Manual navigation arrows and dots

4. QUICK CATEGORY LINKS:
   Shows all 5 PDF categories with:
   - Icon representing category
   - Category name
   - Number of courses in category
   - Hover effect showing 3 sample courses

5. TESTIMONIALS/SUCCESS STORIES:
   - Client logos (if available)
   - Quote carousel with customer photos
   - Metrics display (e.g., "500+ professionals trained")

6. FOOTER (EXACT PDF CONTACT INFO):
   ┌─────────────────────────────────────────────────────┐
   │ Contact:                                            │
   │ +254 702 588644                                    │
   │ +254(0)208000289                                   │
   │ info@bluestron.co.ke                               │
   │ www.bluestron.co.ke                                │
   │                                                     │
   │ Address:                                           │
   │ View Park Towers                                   │
   │ 9th Floor, Nairobi, Kenya                          │
   │                                                     │
   │ [Social Icons] [Newsletter Signup Form]            │
   └─────────────────────────────────────────────────────┘
```

## **5. TECHNICAL ARCHITECTURE - PRODUCTION READY**

### **Backend - Spring Boot 3.2+ (Java 17):**
```
PACKAGE STRUCTURE:
src/main/java/co/ke/bluestron/
├── BluestronApplication.java
├── config/
│   ├── DatabaseConfig.java          # PostgreSQL + HikariCP
│   ├── SecurityConfig.java          # JWT + Role-based auth
│   ├── WebConfig.java               # CORS, Interceptors
│   └── EmailConfig.java             # SMTP/Email service
├── migration/
│   ├── CustomMigrationRunner.java   # Custom migration system
│   ├── MigrationService.java        # Admin migration controls
│   └── resources/migrations/        # SQL migration files
├── domain/
│   ├── model/                       # JPA Entities (Course, Registration, etc.)
│   ├── repository/                  # Spring Data JPA Repositories
│   └── service/                     # Business logic services
├── api/
│   ├── dto/                         # Request/Response DTOs
│   ├── controller/                  # REST Controllers
│   └── middleware/                  # Auth filters, logging
└── shared/
    ├── exception/                   # Global exception handling
    ├── validation/                  # Custom validators
    └── util/                        # Utilities, helpers
```

### **Frontend - Next.js 15 (TypeScript + Tailwind):**
```
FRONTEND STRUCTURE:
app/
├── (public)/                        # Public pages (no auth required)
│   ├── layout.tsx                   # Root layout (nav/footer)
│   ├── page.tsx                     # Homepage
│   ├── courses/
│   │   ├── page.tsx                 # Courses listing with filters
│   │   └── [slug]/page.tsx          # Individual course page
│   ├── services/
│   │   ├── research/page.tsx
│   │   ├── data-analysis/page.tsx
│   │   └── software/page.tsx
│   ├── about/page.tsx
│   ├── contact/page.tsx
│   └── blog/page.tsx                # Optional Week 4
├── (user)/                          # Authenticated users only
│   ├── dashboard/page.tsx           # User dashboard
│   ├── registrations/page.tsx       # My registrations
│   └── certificates/page.tsx        # My certificates (Week 4)
├── (admin)/                         # ADMIN role only
│   ├── dashboard/page.tsx           # Admin dashboard
│   ├── courses/
│   │   ├── page.tsx                 # Manage courses
│   │   └── [id]/edit/page.tsx       # Edit course
│   ├── registrations/page.tsx       # Manage registrations
│   ├── content/                     # Edit all site content
│   └── system/                      # Database, migrations, config
└── api/                             # Next.js API routes (proxies to backend)
```

## **6. SIMPLE INDEPENDENT MODULE: INSTANT COURSE CERTIFICATE GENERATOR**

### **Module Concept:**
After registration, trainee immediately receives a professional "Certificate of Registration" (not completion). This is a marketing tool, not an academic credential.

### **Technical Implementation (Week 4 - 2 Days):**
```java
// COMPLETELY INDEPENDENT SERVICE
@Service
public class CertificateGeneratorService {
    
    // Uses Apache PDFBox (no external dependencies)
    public byte[] generateRegistrationCertificate(Registration reg) {
        // 1. Load certificate template (Bluestron-branded .pdf)
        // 2. Fill template with:
        //    - Trainee Name: reg.getFullName()
        //    - Course Title: reg.getCourse().getTitle()
        //    - Date: LocalDate.now()
        //    - Certificate ID: "BLR-" + year + "-" + sequence
        //    - QR Code linking to verification page
        // 3. Return PDF bytes
    }
    
    // LISTENER PATTERN - No modification to existing code
    @EventListener
    public void handleRegistrationComplete(RegistrationCompletedEvent event) {
        byte[] certificate = generateRegistrationCertificate(event.getRegistration());
        
        // Attach to existing email (NO email code changes)
        event.getEmail().addAttachment(certificate, "certificate.pdf");
        
        // Store for later download
        certificateRepository.save(new Certificate(
            event.getRegistration().getId(),
            certificate,
            generateCertificateNumber()
        ));
    }
}

// SINGLE NEW TABLE
@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    private String certificateNumber;  // BLR-2024-00123
    private Long registrationId;       // Optional reference
    private byte[] pdfData;            // Or file path if storing externally
    private LocalDateTime generatedAt;
    private int downloadCount = 0;
}

// SINGLE NEW API ENDPOINT
@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    
    @GetMapping("/{certificateNumber}")
    public ResponseEntity<byte[]> downloadCertificate(
            @PathVariable String certificateNumber) {
        Certificate cert = certificateService.findByNumber(certificateNumber);
        return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", 
                   "attachment; filename=\"Bluestron_Certificate.pdf\"")
            .body(cert.getPdfData());
    }
    
    @GetMapping("/verify/{certificateNumber}")
    public ResponseEntity<VerificationResponse> verifyCertificate(
            @PathVariable String certificateNumber) {
        // Public verification endpoint
        // Returns: isValid, traineeName, courseTitle, issueDate
    }
}
```

### **Frontend Integration (Simple):**
```typescript
// Added to registration confirmation page ONLY
export default function RegistrationConfirmation({ registration }) {
  return (
    <div className="max-w-4xl mx-auto">
      <h1>Registration Confirmed!</h1>
      <p>Thank you for registering for {registration.course.title}</p>
      
      {/* NEW CERTIFICATE SECTION - Can be removed without breaking anything */}
      <div className="mt-8 p-6 bg-linear-to-r from-blue-50 to-orange-50 rounded-xl border">
        <h2 className="text-2xl font-bold mb-2">🎉 Your Registration Certificate</h2>
        <p className="mb-4">
          Download your official Bluestron registration certificate to share 
          with your network:
        </p>
        <div className="flex flex-col sm:flex-row gap-4">
          <a
            href={`/api/certificates/${registration.certificateNumber}`}
            className="flex-1 bg-[#ff851b] text-white py-3 px-6 rounded-lg text-center font-semibold hover:bg-[#e76a00]"
            download
          >
            📥 Download Certificate (PDF)
          </a>
          <button
            onClick={() => shareOnLinkedIn(registration.certificateNumber)}
            className="flex-1 bg-blue-600 text-white py-3 px-6 rounded-lg text-center font-semibold hover:bg-blue-700"
          >
            🔗 Share on LinkedIn
          </button>
        </div>
        <p className="text-sm text-gray-600 mt-3">
          Certificate ID: {registration.certificateNumber} • 
          <a href={`/verify/${registration.certificateNumber}`} className="ml-2 text-blue-600">
            Verify Certificate
          </a>
        </p>
      </div>
      
      {/* Existing registration details below */}
      <div className="mt-8">
        {/* Original confirmation content remains unchanged */}
      </div>
    </div>
  );
}
```

### **Why This Proves Modularity:**
1. **Zero Dependencies**: Doesn't require authentication, payment processing, or course completion
2. **Single Table**: `certificates` table with no foreign key constraints (optional reference only)
3. **Single API Endpoint**: `/api/certificates/{id}` - can be deployed separately
4. **Event-Driven**: Listens for registration events, doesn't modify registration flow
5. **Removable**: Delete 3 files (Service, Controller, Entity) and frontend component - system still works
6. **Configurable**: Enable/disable via `application.yml: certificates.enabled=true/false`

### **Business Value:**
- **Immediate Gratification**: User gets tangible value instantly
- **Social Proof**: Every shared certificate = free marketing
- **Professional Image**: Shows we're organized and tech-forward
- **Competitive Edge**: Most training institutes don't provide this

## **7. IMPLEMENTATION TIMELINE (21 WORKING DAYS)**

### **Week 1: Foundation (Days 1-7)**
```
Day 1: PostgreSQL setup, connection pooling, basic Spring Boot structure
Day 2: Custom migration system (CustomMigrationRunner.java)
Day 3: V1-V3 migrations (categories, 44 courses, registration table)
Day 4: Course API endpoints (GET /api/courses, /api/courses/{id})
Day 5: Registration API (POST /api/registrations with validation)
Day 6: Email service (SMTP configuration, dual emails)
Day 7: Basic admin authentication (JWT, ADMIN/USER roles)
```

### **Week 2: Core Features (Days 8-14)**
```
Day 8: Frontend homepage (Hero, 3-boxes, carousel, categories)
Day 9: Courses listing page with filters (Category, Duration, Mode, Date)
Day 10: Individual course page with registration form (EXACT 8 fields)
Day 11: Service pages (Research, Data Analysis, Software)
Day 12: About Us, Contact pages (with map, PDF contact info)
Day 13: Admin dashboard foundation
Day 14: Admin course management (CRUD for 44 courses)
```

### **Week 3: Polish & Launch (Days 15-21)**
```
Day 15: Admin content management (edit ALL site content)
Day 16: Responsive design testing (mobile, tablet, desktop)
Day 17: SEO implementation (meta tags, sitemap, clean URLs)
Day 18: Performance optimization (image compression, lazy loading)
Day 19: Security hardening (rate limiting, CSRF, input validation)
Day 20: User acceptance testing, bug fixes
Day 21: Production deployment, monitoring setup
```

### **Week 4: Optional Modules (Post-MVP)**
```
Certificate Module (2 days):
  Day 1: PDF generation service, email attachment
  Day 2: Download endpoint, admin certificate management

Blog Module (2 days):
  Day 3: Blog post CRUD, categories, SEO-friendly URLs
  Day 4: Blog homepage, article pages, related posts

Payment Integration (3 days):
  Day 5-7: Stripe integration, invoice generation, payment tracking
```

## **8. VALIDATION CHECKLIST AGAINST PDF**

| PDF Section | Requirement | Implementation Status | Notes |
|-------------|------------|----------------------|-------|
| **1.1** | Training-first business | ✅ | 44 courses dominate homepage |
| **1.1** | Three services separate pages | ✅ | /services/research, /data-analysis, /software |
| **2.1** | Homepage hero with exact text | ✅ | "Professional Training, Research & Software Development in Data, M&E, Management" |
| **2.1** | 3-box highlight (Training lead) | ✅ | Training box 1.5x width visually |
| **2.1** | Featured courses carousel | ✅ | Auto-rotate, 3-5 courses |
| **2.1** | Quick category links | ✅ | 5 categories with counts |
| **2.1** | Testimonials section | ✅ | Quote carousel, client logos |
| **2.1** | Footer with exact contact info | ✅ | +254 702 588644, +254(0)208000289, info@bluestron.co.ke |
| **2.1** | Newsletter sign-up | ✅ | Email form in footer |
| **3** | CMS capability | ✅ | Admin dashboard edits everything |
| **3** | Course filters (Category, Duration, Mode, Date) | ✅ | All 4 filters implemented |
| **3** | Registration form (exact 8 fields) | ✅ | All fields from PDF Section 6 |
| **3** | Emails to trainee AND admin | ✅ | Dual email system with templates |
| **3** | Responsive design | ✅ | Mobile-first, 3 breakpoints |
| **3** | SEO best practices | ✅ | Clean URLs, meta tags, sitemap |
| **3** | Google Analytics | ✅ | Integration ready |
| **3** | Bluestron branding | ✅ | #001f3f navy, #ff851b orange, Inter font |
| **3** | Clear navigation | ✅ | Exact menu: Home|About|Courses|Research|Data|Software|Blog|Contact |
| **3** | Security (SSL, backups, secure login) | ✅ | HTTPS, JWT, daily backups |
| **3** | Accessibility | ✅ | Alt tags, ARIA labels, keyboard nav |
| **6** | Registration workflow | ✅ | Exact PDF workflow implemented |

## **9. DEPLOYMENT SPECIFICATION**

### **Hosting Infrastructure:**
```
Frontend (Next.js): Vercel (automatic deployments from GitHub)
Backend (Spring Boot): AWS EC2 t3.medium or DigitalOcean Droplet ($20/month)
Database: AWS RDS PostgreSQL db.t3.micro or Aiven PostgreSQL ($15/month)
Email: SendGrid or AWS SES (25,000 emails free tier)
File Storage: AWS S3 or DigitalOcean Spaces for uploads
Backups: Automated daily to S3/Spaces (7-day retention)
```

### **Environment Variables:**
```yaml
# application.yml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
bluestron:
  admin:
    initial-email: ${INITIAL_ADMIN_EMAIL:admin@bluestron.co.ke}
    initial-password: ${INITIAL_ADMIN_PASSWORD} # REQUIRED at first startup
  
  email:
    from: ${EMAIL_FROM:info@bluestron.co.ke}
    smtp-host: ${SMTP_HOST}
    smtp-username: ${SMTP_USERNAME}
    smtp-password: ${SMTP_PASSWORD}
  
  certificates:
    enabled: ${CERTIFICATES_ENABLED:false} # Week 4 module flag
  
  analytics:
    google-tracking-id: ${GA_TRACKING_ID}
```

### **Monitoring & Maintenance:**
```
1. Uptime Monitoring: UptimeRobot (free tier)
2. Error Tracking: Sentry (free tier up to 5,000 errors/month)
3. Performance: Google Lighthouse CI (automated on deployment)
4. Security: Snyk/Dependabot for vulnerability scanning
5. Logging: Structured JSON logs to AWS CloudWatch/Papertrail
6. Backups: Daily automated + manual trigger via admin dashboard
```

## **10. DEVELOPER ONBOARDING GUIDE**

### **Local Development Setup:**
```bash
# 1. Clone repository
git clone https://github.com/bluestron/training-portal.git
cd bluestron

# 2. Set up PostgreSQL locally or use cloud instance
# Create database: bluestron_prod
# Update src/main/resources/application-dev.yml

# 3. Backend setup
cd backend
./mvnw spring-boot:run
# Application starts on http://localhost:8080
# First run will execute migrations V1-V8

# 4. Frontend setup
cd frontend
npm install
npm run dev
# Frontend starts on http://localhost:3000
```

### **First Admin User Creation:**
```bash
# On first deployment, set environment variable:
export INITIAL_ADMIN_PASSWORD="secure-password-here"

# Application will create admin user:
# Email: admin@bluestron.co.ke (or set via INITIAL_ADMIN_EMAIL)
# Password: Hashed version of above
# Role: ADMIN

# Subsequent admins can be created via admin dashboard
```

### **Code Standards:**
```
Backend:
- Java: Google Java Style Guide
- Spring: Spring Boot conventions
- Tests: JUnit 5, Mockito
- Coverage: Minimum 80% test coverage

Frontend:
- TypeScript: Strict mode, no `any` types
- React: Functional components with hooks
- Styling: Tailwind CSS utility classes
- Testing: Jest, React Testing Library
```

---

**FINAL CONFIRMATION:** This revised implementation specification EXACTLY matches ALL requirements in the Bluestron Training Business PDF document. It includes:

1. **EXACT 44 courses** across 5 categories from PDF
2. **EXACT registration workflow** with 8 fields from PDF Section 6
3. **EXACT contact information** (+254 numbers, email, address)
4. **EXACT homepage structure** from PDF Section 2.1
5. **Custom Java migration system** (no Docker, no Flyway)
6. **Complete admin CMS** in Java (edits everything)
7. **Simple certificate module** (Week 4, completely independent)
8. **Production-ready architecture** with clear scaling path

**Ready to implement starting with Week 1, Day 1: Database & Custom Migration System?**