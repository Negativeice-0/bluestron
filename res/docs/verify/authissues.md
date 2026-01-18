# just in case

Got it — let’s make this crystal clear and runnable.  

---

## 📂 AuthController Imports and Structure

Here’s a **complete AuthController** with all imports, request/response DTOs, and getters/setters:

```java
package co.ke.bluestron.bsapi.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.ke.bluestron.bsapi.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // TODO: replace with real user validation (DB lookup)
        if ("admin@bluestron.com".equals(request.getEmail()) &&
            "admin123".equals(request.getPassword())) {
            String token = jwtService.generateToken(request.getEmail(), "ADMIN");
            return ResponseEntity.ok(new TokenResponse(token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // DTO for login request
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // DTO for token response
    public static class TokenResponse {
        private String token;

        public TokenResponse(String token) { this.token = token; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
```

- **Imports**: `org.springframework.http.ResponseEntity`, `org.springframework.web.bind.annotation.*`, and your own `JwtService`.
- **Getters/Setters**: Provided in `LoginRequest` and `TokenResponse`.

---

## 🧪 Verbose Curl Commands for Testing

Here’s a full suite of curl commands you can run against your backend:

### 1. Login (get JWT)

```bash
curl -v -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@bluestron.com","password":"admin123"}'
```

Expected: JSON with `token`.

---

### 2. Access Admin Dashboard Stats (requires JWT)

```bash
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."  # paste token from login

curl -v -X GET http://localhost:8080/api/admin/dashboard/stats \
  -H "Authorization: Bearer $TOKEN"
```

Expected: JSON with `totalCourses`, `totalRegistrations`, `totalPosts`.

---

### 3. Public Endpoints

```bash
curl -v -X GET http://localhost:8080/api/categories
curl -v -X GET http://localhost:8080/api/courses
curl -v -X GET http://localhost:8080/api/blog
curl -v -X GET http://localhost:8080/api/testimonials
```

Expected: JSON arrays of categories, courses, blog posts, testimonials.

---

### 4. Media Streaming

```bash
curl -v -X GET http://localhost:8080/api/media/1/stream
```

Expected: File stream (e.g. video or image). Right now it’s a stub.

---

### 5. Bad Login (negative test)

```bash
curl -v -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@bluestron.com","password":"wrong"}'
```

Expected: `401 Unauthorized`.

---

## 6. Missing Token (negative test)

```bash
curl -v -X GET http://localhost:8080/api/admin/dashboard/stats
```

Expected: `403 Forbidden`.

---

## 🔑 Summary

- **AuthController** now has imports, DTOs, getters/setters.  
- **Curl suite** covers login, admin stats, public endpoints, media, and negative cases.  
- Run these to confirm each controller works and security is enforced.  

👉 Next step: once you run these, we’ll know exactly which controllers/services still need fixes. Would you like me to also scaffold a **DashboardController** with dummy stats so your `/api/admin/dashboard/stats` curl test passes immediately?
