Here’s exactly what you asked for — the **curl command that would have shown you the 401 issue clearly**, plus a **minimal SecurityConfig** that opens endpoints for testing (JWT can be added later), and a concise summary.

---

## 🔍 Full verification curl command

This version prints headers, status code, and body so you immediately see if it’s `401 Unauthorized` or something else:

```bash
curl -v -i -s -w "\nHTTP Status: %{http_code}\n" http://localhost:8080/api/categories
```

- `-v` → verbose, shows request/response details.  
- `-i` → include headers in output.  
- `-s` → silent progress meter.  
- `-w` → append HTTP status code at the end.  

This would have shown you the `WWW-Authenticate: Basic realm="Realm"` header and `401` status, making it obvious that Spring Security was blocking the request.

---

## 🔐 Minimal SecurityConfig for testing (no JWT yet)

```java
// src/main/java/com/bluestron/config/SecurityConfig.java
package com.bluestron.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for API testing
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // allow all endpoints for now
            )
            .httpBasic(httpBasic -> httpBasic.disable()); // disable Basic Auth
        return http.build();
    }
}
```

- **All endpoints are open** for testing.  
- **Basic Auth disabled** so you don’t get the `401 Unauthorized`.  
- **JWT filter not added yet** — you’ll plug that in later when you’re ready to secure `/api/admin` and other sensitive routes.

---

## 📌 Summary

- The detailed curl command (`curl -v -i -s -w ...`) would have immediately shown the `401 Unauthorized` and `WWW-Authenticate` header, pointing to Spring Security as the cause.  
- For development/testing, you can use the minimal `SecurityConfig` above to **permit all requests** and disable Basic Auth.  
- Later, for production, you’ll replace `.anyRequest().permitAll()` with **fine‑grained rules** (public endpoints open, admin endpoints protected) and add a **JWT filter** for authentication/authorization.  

---

Would you like me to also sketch the **production‑ready SecurityConfig skeleton** (with placeholders for JWT) so you can see exactly how to evolve this testing config into a secure one later?