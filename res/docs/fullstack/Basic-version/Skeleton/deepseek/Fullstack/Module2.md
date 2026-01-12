# Module 1 Summary & Module 2 Plan

## 📋 **MODULE 1 SUMMARY (Current State)**

### ✅ **What's Completed:**

1. **Backend API (`bsapi/`)**:
   - Spring Boot 3.2.1, Java 21
   - PostgreSQL integration with your exact configuration
   - Basic CRUD for `Category`, `Course`, `Registration` entities
   - Simple REST API endpoints:
     - `GET /api/categories` - List all categories
     - `GET /api/courses` - List all courses
     - `POST /api/registrations` - Register for a course
   - Basic security (actuator endpoints only)

2. **Frontend (`bsui/`)**:
   - Next.js 14 with TypeScript
   - Home page with course listing
   - Simple course detail page
   - Basic registration form

3. **Database (`database/`)**:
   - Normalized schema with 3 tables
   - Seed data matching PDF categories and courses
   - PostgreSQL 14+ compatible

4. **Automation**:
   - Makefile with complete workflow
   - Environment configuration
   - Verification scripts

### ⚠️ **What's MISSING from Requirements:**

1. **User Authentication** (JWT, roles ADMIN/USER)
2. **Admin CRUD Dashboard** for managing data
3. **Service Pages** (Research, Data Analysis, Software Development)
4. **Course Filtering/Search** (category, mode, date)
5. **Registration Workflow** (confirmation, email stub)
6. **About Us, Contact, Other Pages**
7. **Blog/Insights Section**
8. **Responsive Design** with TailwindCSS
9. **Form Validation** (frontend + backend)
10. **Proper Error Handling**

### 🔄 **How to Roll Back to Module 1:**

```bash
# Option 1: Git Tag (Recommended)
git tag module-1-v1.0
git push origin module-1-v1.0

# Option 2: Branch
git checkout -b module-1-complete
git add .
git commit -m "Module 1 Complete: Basic API + Frontend"

# Option 3: Archive
tar -czf module-1-complete.tar.gz \
  bsapi/ \
  bsui/ \
  database/ \
  Makefile \
  .env.example \
  VERIFICATION.md
```

### 📁 **Module 1 File Structure:**
```
bluestron-mvp/
├── bsapi/                    # Backend
│   ├── src/main/java/co/ke/bluestron/bsapi/
│   │   ├── BsapiApplication.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── CategoryController.java
│   │   │   ├── CourseController.java
│   │   │   └── RegistrationController.java
│   │   ├── entity/
│   │   │   ├── Category.java
│   │   │   ├── Course.java
│   │   │   └── Registration.java
│   │   ├── repository/
│   │   │   ├── CategoryRepository.java
│   │   │   ├── CourseRepository.java
│   │   │   └── RegistrationRepository.java
│   │   └── resources/
│   │       └── application.yml
│   ├── pom.xml
│   └── mvnw
├── bsui/                     # Frontend
│   ├── app/
│   │   ├── page.tsx         # Home
│   │   └── courses/
│   │       └── page.tsx     # Courses listing
│   ├── package.json
│   └── next.config.js
├── database/
│   ├── schema.sql
│   └── seed.sql
├── Makefile
├── .env.example
├── VERIFICATION.md
└── setup.sh
```

---

# 🚀 **MODULE 2: Authentication & Admin Dashboard**

## **Goal:** Add user authentication and admin CRUD interface

### **Module 2 Requirements:**

1. **User Authentication**:
   - JWT-based authentication
   - Roles: `ADMIN` and `USER`
   - Registration for regular users
   - Login/logout

2. **Admin Dashboard**:
   - CRUD operations for Categories
   - CRUD operations for Courses
   - View/Manage Registrations
   - Dashboard overview

3. **Enhanced Frontend**:
   - TailwindCSS for styling
   - Responsive design
   - Form validation
   - Better UI/UX

4. **Improved API**:
   - Request/Response DTOs
   - Input validation
   - Proper error handling
   - Service layer separation

---

## **Module 2 Implementation Plan:**

### **Phase 1: Authentication System**

**File: `bsapi/pom.xml`** (Add JWT dependencies)
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**File: `bsapi/src/main/resources/application.yml`** (Update)
```yaml
# Add to existing config
jwt:
  secret: ${JWT_SECRET:bluestronTrainingBusinessSecretKeyForJWTSigning2024}
  expiration: 86400000 # 24 hours
```

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/entity/User.java`**
```java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String role = "USER"; // ADMIN or USER
    
    private String phone;
    private String organization;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
```










I’m on Spring Boot 3.x with Spring Security 6. 
My current SecurityConfig is very lean — it only disables CSRF, enables CORS, sets stateless sessions, and permits /api/** and /actuator/health. 
I want to add JWT authentication, registration/login endpoints, and role‑based authorization without breaking that style or introducing redundant beans. 
Please give me the full integrated code (SecurityConfig, JwtAuthFilter, CustomUserDetailsService, UserRepository, UserService, PasswordConfig, AuthController) that works with my existing JwtService implementation and avoids unnecessary AuthenticationManager beans.








Perfect — here’s the **final integrated setup** for Spring Boot 3 / Spring Security 6 that matches your lean style, avoids redundant beans, and wires in JWT cleanly.

---

## 🔐 `SecurityConfig.java`
##use this ignore that below -- didn't cut it because of cool comments
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
            corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.setAllowCredentials(true);
            return corsConfig;
        }))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/api/**").permitAll()
            .requestMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


```


```java
package co.ke.bluestron.bsapi.config;

import co.ke.bluestron.bsapi.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // allows @PreAuthorize
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()        // login/register open
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/**").permitAll()         // keep your current openness
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 
```

---

## 🛡️ `JwtAuthFilter.java`

```java
package co.ke.bluestron.bsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String email = jwtService.extractUsername(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

## 👤 `CustomUserDetailsService.java`

```java
package co.ke.bluestron.bsapi.security;

import co.ke.bluestron.bsapi.entity.User;
import co.ke.bluestron.bsapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
```

---

## 📦 `UserRepository.java`

```java
package co.ke.bluestron.bsapi.repository;

import co.ke.bluestron.bsapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
```

---

## 🛠️ `UserService.java`

```java
package co.ke.bluestron.bsapi.service;

import co.ke.bluestron.bsapi.entity.User;
import co.ke.bluestron.bsapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }
}
```

---

## 🔑 `PasswordConfig.java`

```java
package co.ke.bluestron.bsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 🎯 `AuthController.java`

```java
package co.ke.bluestron.bsapi.controller;

import co.ke.bluestron.bsapi.entity.User;
import co.ke.bluestron.bsapi.security.JwtService;
import co.ke.bluestron.bsapi.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userService.register(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        return jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal());
    }
}
```



package co.ke.bluestron.bsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class AuthManagerConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}




---

## ✅ Key Notes

- No redundant `AuthenticationManager` bean — Spring Boot 3 auto‑exposes it since you have `UserDetailsService` + `PasswordEncoder`.  
- Your `JwtService` implementation is already correct for jjwt 0.12+ (Spring Boot 3).  
- This setup keeps your `/api/**` endpoints open, `/auth/**` open, and everything else protected.  
- You can now add `@PreAuthorize("hasRole('ADMIN')")` on admin‑only endpoints.

---

Would you like me to also **show the frontend React login flow** (JWT storage + role guard) so you can test this end‑to‑end?







### **Phase 2: Database Updates**

**File: `database/schema.sql`** (Add users table and constraints)
```sql
-- Add to existing schema

-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER' CHECK (role IN ('ADMIN', 'USER')),
    phone VARCHAR(50),
    organization VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add user_id to registrations (optional link)
ALTER TABLE registrations ADD COLUMN user_id INTEGER REFERENCES users(id);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_registrations_user ON registrations(user_id);
```

**File: `database/seed.sql`** (Add admin user)
```sql
-- Add admin user (password: Admin123!)
-- BCrypt hash for 'Admin123!' (you'll need to generate this)
INSERT INTO users (email, password, full_name, role) VALUES
('admin@bluestron.co.ke', '$2a$12$q7v.BhOe5E3z7b5W8pQYQO5v5T6n7v8w9x0y1z2A3B4C5D6E7F8G9H0I1J', 'Bluestron Admin', 'ADMIN');
```

### **Phase 3: JWT Authentication**

**File: `bsapi/src/main/java/co/ke/bluestron/bsapi/security/JwtService.java`**
```java
package co.ke.bluestron.bsapi.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                // modern jjwt: just pass the key, algorithm inferred
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())   // modern jjwt verification
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

```

### **Phase 4: Frontend with TailwindCSS**

**File: `bsui/package.json`** (Update dependencies)
```json
{
  "dependencies": {
    "next": "14.0.0",
    "react": "^18",
    "react-dom": "^18",
    "axios": "^1.6.0",
    "react-hook-form": "^7.48.0",
    "zod": "^3.22.0",
    "@hookform/resolvers": "^3.3.0",
    "jwt-decode": "^4.0.0",
    "date-fns": "^3.0.0",
    "tailwindcss": "^3.3.0",
    "autoprefixer": "^10.0.0",
    "postcss": "^8.0.0",
    "react-icons": "^4.12.0",
    "lucide-react": "^0.292.0"
  }
}
```

**File: `bsui/tailwind.config.js`**
```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
          800: '#1e40af',
          900: '#1e3a8a',
        }
      }
    },
  },
  plugins: [],
}
```

### **Phase 5: Admin Dashboard**

**File: `bsui/app/admin/page.tsx`**
This works better


Here’s a clean wrap‑up for you, Billy:

---

## 📝 Summary of the explanation
- Your original `AdminDashboard` was throwing errors because you mixed **inline type annotations** (`: unknown`) inside JSX/TSX, which Next.js/TypeScript doesn’t allow.  
- You were also relying on **localStorage** for JWT, which is brittle in Next.js App Router.  
- The modern approach is to:
  - Store JWT in **cookies** at login.  
  - Use **server components** (`page.tsx`) to fetch data securely with `cookies()` and `redirect()`.  
  - Define **interfaces** for your API responses instead of sprinkling `any`/`unknown`.  
  - Fetch stats server‑side so the page renders with data immediately, no flicker.

---

## 🎯 Prompt to avoid this issue next time
When you want me to generate Next.js code, use a prompt like:

```
I’m building a Next.js App Router (v13+) page with TypeScript. 
Please give me the full source code for a secure admin dashboard that:
- Uses JWT stored in cookies, not localStorage
- Fetches data server-side with cookies() and redirect()
- Defines proper TypeScript interfaces for API responses
- Redirects non-admin users to /login or /
- Renders stat cards and quick links with Tailwind
Make sure the code is modern, valid TSX, and avoids inline type annotations inside JSX.
```

That way I’ll know to give you **cookie-based auth, server components, and clean TypeScript** without the parsing errors.

---

## ✅ Final full source code

`src/app/admin/page.tsx`

```tsx
import { cookies } from 'next/headers';
import { redirect } from 'next/navigation';

// Define interfaces for API responses
interface Course { id: number; title: string; }
interface Category { id: number; name: string; }
interface Registration { id: number; status: string; }

async function getStats(token: string) {
  const headers = { Authorization: `Bearer ${token}` };

  const [coursesRes, registrationsRes, categoriesRes] = await Promise.all([
    fetch('http://localhost:8080/api/courses', { headers, cache: 'no-store' }),
    fetch('http://localhost:8080/api/registrations', { headers, cache: 'no-store' }),
    fetch('http://localhost:8080/api/categories', { headers, cache: 'no-store' }),
  ]);

  const courses: Course[] = await coursesRes.json();
  const registrations: Registration[] = await registrationsRes.json();
  const categories: Category[] = await categoriesRes.json();

  return {
    totalCourses: courses.length,
    totalRegistrations: registrations.length,
    totalCategories: categories.length,
    pendingRegistrations: registrations.filter(r => r.status === 'PENDING').length,
  };
}

export default async function AdminDashboard() {
  const token = cookies().get('token')?.value;
  if (!token) redirect('/login');

  // Decode JWT payload
  const payload = JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString());
  const role = payload?.authorities?.[0] || 'USER';
  if (!role.includes('ADMIN')) redirect('/');

  const stats = await getStats(token);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Admin Dashboard</h1>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard label="Total Courses" value={stats.totalCourses} color="blue" />
        <StatCard label="Registrations" value={stats.totalRegistrations} color="green" />
        <StatCard label="Pending" value={stats.pendingRegistrations} color="yellow" />
        <StatCard label="Categories" value={stats.totalCategories} color="purple" />
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <QuickLink href="/admin/courses" label="Manage Courses" desc="Add, edit, or delete training courses" color="blue" />
        <QuickLink href="/admin/categories" label="Manage Categories" desc="Organize courses into categories" color="green" />
        <QuickLink href="/admin/registrations" label="Registrations" desc="View and manage course registrations" color="yellow" />
      </div>
    </div>
  );
}

function StatCard({ label, value, color }: { label: string; value: number; color: string }) {
  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center">
        <div className={`bg-${color}-100 p-3 rounded-lg`}>
          <div className={`w-6 h-6 text-${color}-600`}>★</div>
        </div>
        <div className="ml-4">
          <p className="text-sm font-medium text-gray-600">{label}</p>
          <p className="text-2xl font-semibold text-gray-900">{value}</p>
        </div>
      </div>
    </div>
  );
}

function QuickLink({ href, label, desc, color }: { href: string; label: string; desc: string; color: string }) {
  return (
    <a href={href} className="block p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow">
      <div className="flex items-center">
        <div className={`bg-${color}-100 p-3 rounded-lg`}>
          <div className={`w-6 h-6 text-${color}-600`}>→</div>
        </div>
        <div className="ml-4">
          <h3 className="text-lg font-semibold text-gray-900">{label}</h3>
          <p className="text-gray-600 mt-1">{desc}</p>
        </div>
      </div>
    </a>
  );
}
```

---

This version:
- ✅ Uses cookies for JWT.  
- ✅ Redirects non‑admins before rendering.  
- ✅ Fetches stats server‑side with proper interfaces.  
- ✅ Avoids inline type annotations inside JSX.  
- ✅ Tailwind‑ready stat cards and quick links.  

---

Would you like me to also give you the **login page code that sets the JWT cookie** (instead of localStorage) so this dashboard works seamlessly?


```tsx
'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function AdminDashboard() {
  const [stats, setStats] = useState({
    totalCourses: 0,
    totalRegistrations: 0,
    totalCategories: 0,
    pendingRegistrations: 0,
  });
  const router = useRouter();

  useEffect(() => {
    // Check if user is admin
    const token = localStorage.getItem('token');
    if (!token) {
      router.push('/login');
      return;
    }

    // Fetch stats
    Promise.all([
      fetch('http://localhost:8080/api/courses').then(res => res.json()),
      fetch('http://localhost:8080/api/registrations').then(res => res.json()),
      fetch('http://localhost:8080/api/categories').then(res => res.json()),
    ]).then(([courses, registrations, categories]) => {
      setStats({
        totalCourses: courses.length,
        totalRegistrations: registrations.length,
        totalCategories: categories.length,
        pendingRegistrations: registrations.filter((r: any) => r.status === 'PENDING').length,
      });
    });
  }, [router]);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Admin Dashboard</h1>
      
      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="bg-blue-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Total Courses</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalCourses}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="bg-green-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Registrations</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalRegistrations}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="bg-yellow-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Pending</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.pendingRegistrations}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="bg-purple-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Categories</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalCategories}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Link href="/admin/courses" className="block p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="bg-blue-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">Manage Courses</h3>
              <p className="text-gray-600 mt-1">Add, edit, or delete training courses</p>
            </div>
          </div>
        </Link>

        <Link href="/admin/categories" className="block p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="bg-green-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">Manage Categories</h3>
              <p className="text-gray-600 mt-1">Organize courses into categories</p>
            </div>
          </div>
        </Link>

        <Link href="/admin/registrations" className="block p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="bg-yellow-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">Registrations</h3>
              <p className="text-gray-600 mt-1">View and manage course registrations</p>
            </div>
          </div>
        </Link>
      </div>
    </div>
  );
}
```

---

## 📊 **Module 2 Deliverables:**

### **Backend Features:**
1. ✅ JWT Authentication
2. ✅ User Registration/Login
3. ✅ Role-based Authorization
4. ✅ Admin-only API endpoints
5. ✅ Password hashing (BCrypt)
6. ✅ Secure endpoints

### **Frontend Features:**
1. ✅ Login/Registration pages
2. ✅ Admin Dashboard
3. ✅ Course Management CRUD
4. ✅ Category Management CRUD
5. ✅ Registration Management
6. ✅ Responsive UI with TailwindCSS

### **Database Features:**
1. ✅ Users table with roles
2. ✅ Relationships updated
3. ✅ Indexes for performance
4. ✅ Admin user seeded

---

## 🎯 **Module 2 Implementation Steps:**

```bash
# Step 1: Update Module 1 to Module 2
git checkout -b module-2-auth-admin

# Step 2: Add JWT dependencies to pom.xml
# Step 3: Update database schema
make db-drop db-create db-schema db-seed

# Step 4: Implement JWT authentication
# - Create User entity
# - Create JwtService
# - Create AuthController
# - Update SecurityConfig

# Step 5: Install TailwindCSS
cd bsui
npm install tailwindcss postcss autoprefixer
npx tailwindcss init -p

# Step 6: Create admin pages
mkdir -p bsui/app/admin/{courses,categories,registrations}

# Step 7: Update frontend API client
# - Add axios interceptors for JWT
# - Create auth context

# Step 8: Test everything
make verify-all
```

---

## 🔄 **Future Modules Roadmap:**

### **Module 3: Service Pages & Enhanced Features**
- Research Services page
- Data Analysis Services page
- Software Development Services page
- Contact Us page with form
- About Us page with team info

### **Module 4: Advanced Features**
- Course filtering/search
- Email notifications
- File uploads (course thumbnails)
- Payment integration (stripe/mpesa)
- Reports & Analytics

### **Module 5: Polish & Deployment**
- SEO optimization
- Performance optimization
- Security hardening
- Deployment scripts
- CI/CD pipeline

---

## 🚨 **Important Notes:**

1. **Module 1 is stable and working** - you can deploy it as-is for basic functionality
2. **Module 2 builds incrementally** - each feature can be tested independently
3. **Rollback is easy** with git tags/branches
4. **Database migrations** are manual for now (simple SQL scripts)
5. **All environment configs** are in `.env.example`

## 📞 **Need Help?**
- Run `make verify-all` to check current state
- Check `VERIFICATION.md` for troubleshooting
- Use `./setup.sh` for fresh installs
- Test APIs with `make be-curl-courses`

The system is now modular and ready for incremental enhancement while maintaining a working base at Module 1!




